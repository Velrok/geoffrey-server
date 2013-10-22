(ns server.core
  (:require [compojure.handler :as handler]
            [compojure.route   :as route]
            [server.views      :as views]
            [clojure.data.json :as json]
            [server.messaging  :as msging]
            [clojure.set          :refer [difference]]
            [ring.middleware.json :refer :all]
            [compojure.core       :refer :all]))


(def files (atom {}))
(def inboxes (atom {}))

(defn reset-clients-files! [files-atom client new-files]
  (swap! files-atom
         (fn [d]
           (assoc d client new-files))))

(defn validate-files
  "Returns true if valid and an error message if false."
  [files-data]
  (cond
    (not (vector? files-data)) "Content must be a JSON vector."
    (not (every? map? files-data)) "JSON vector must contain objects of md5-hash -> filename pairs."
    :else true))

(defroutes main-routes
  (GET "/" []
       (views/files-overview @files))

  (GET "/debug/inboxes" []
       (json/write-str @inboxes))

  (GET "/share/:md5"
       [md5]
       (msging/broadcast! @inboxes
                          (msging/->Message :share md5))
       {:status 501})

  (PUT "/:client/files" [client :as r]
       (msging/create-inbox client)
       (let [send-data (->> r
                            :body
                            json/read-str)
             validation-result (validate-files send-data)]
         (if (= true validation-result)
           (do
             (reset-clients-files! files client send-data)
             {:status 200 :body "ok"})
           {:status 400 :body validation-result})))

  (GET "/:client/messages" [client]
       (let [messages (@inboxes client)]
         (swap! inboxes
                (fn [m]
                  (update-in m [client]
                             (fn [s]
                               (difference s messages)))))
         (json/write-str messages)))

  (route/not-found "<h1>Page not found</h1>"))


(def app
  (-> main-routes
      wrap-json-body
      handler/site))
