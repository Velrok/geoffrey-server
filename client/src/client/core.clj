(ns client.core
  (:require [clojure.string :as string]
            [digest :as digest]
            [clojure.java.io :as io]
            [clojure.data.json :as json]
            [clojure.core.async :refer :all]
            [me.raynes.fs :as fs]
            [clj-http.client :as http]
            [environ.core :refer [env]])
  (:gen-class))

(defn filepath->metadata [filepath]
  {:pre [[string? filepath]]}
  (let [pattern (re-pattern
                  (str
                    "(.*/)?"                     ; path / prefix
                    "([\\w\\(\\) ]*)"            ; show name
                    " - [Ss]?(\\d*)[Eex]?(\\d*)" ; season# episode#
                    "( - ([^-]*))?"              ; episode title
                    "( - ([^-]*))?"              ; quality
                    "(.*)"                       ; other suff
                    "\\.(\\w+)"))                ; file extension
        groups (re-matches pattern filepath)
        md5 (try (digest/md5 (io/as-file filepath))
             (catch java.io.IOException e
               nil))]
    (if (not (nil? groups))
      {:filepath filepath
       :showname (groups 2)
       :md5      md5
       :title    (string/trim (groups 6))
       :season#  (Integer/parseInt (groups 3))
       :episode# (Integer/parseInt (groups 4))
       :ext      (groups 10)}
      {})))

(defn list-media-files [filepath]
  {:pre  [[string? filepath]]
   :post [[every? % map?]]}
  (->> (fs/iterate-dir filepath)
       (map (fn [[root _ files]]
              (let [root-path (.getAbsolutePath root)]
                (for [f files]
                  (->> f
                       str
                       (str root-path "/")
                       fs/normalized-path)))))
       flatten
       (map str)
       (map filepath->metadata)
       (filter #(not (empty? %)))))

(def running (atom true))

(defn stop-all-threads []
  (reset! running false))

(defn watch-shows-folder! [folder out-chan ms-delay]
  (go
    (while @running
      (>! out-chan
          (list-media-files folder))
      (<! (timeout ms-delay)))))


(defn on-change-trigger [in out]
  (let [last-value (atom nil)]
    (go
      (while @running
        (let [current-value (<! in)]
          (if (not (= @last-value
                      current-value))
            (do
              (>! out current-value)
              (reset! last-value current-value))))))))

(defn print-chanel [c]
  (go
    (while @running
      (println (<! c)))))

(defn upload-show-data [geoffrey-server-address geoffrey-client-name shows-data]
  (let [url (str geoffrey-server-address "/"
                 geoffrey-client-name
                 "/shows")
        json-data (json/write-str shows-data)]
    (println "uploading to " url)
    (println "data" json-data)
    (try
      (http/put url
                {:content-type :json
                  :form-params json-data})
      (catch java.net.ConnectException e
        (println "ERROR: upload failed: " (.getMessage e)))
      (catch org.apache.http.NoHttpResponseException e
        (println "ERROR: upload failed: " (.getMessage e)))
      (catch clojure.lang.ExceptionInfo e
        (println "ERROR: upload failed: " (.getMessage e))))))

(defn -main [& args]
  (let [geoffrey-server-address (env :geoffrey-server "http://localhost:3000")
        geoffrey-client-name    (env :geoffrey-client-name
                                     (.getHostName
                                       (java.net.InetAddress/getLocalHost)))
        shows-dir               (env :geoffrey-shows-dir "./fixtures/fs/shows/")
        shows-data              (chan)
        shows-data-changes      (chan)]
    (reset! running true)
    (watch-shows-folder! shows-dir shows-data 3000)
    (on-change-trigger shows-data shows-data-changes)
    (while @running
      (upload-show-data geoffrey-server-address
                        geoffrey-client-name
                        (<!! shows-data)))))
