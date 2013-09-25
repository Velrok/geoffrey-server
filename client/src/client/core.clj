(ns client.core
  (:require [clojure.string :as string]
            [me.raynes.fs :as fs]))


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
        groups (re-matches pattern filepath)]
    (if (not (nil? groups))
      {:filepath filepath
       :showname (groups 2)
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
                       fs/absolute-path
                       (str root-path "/")
                       fs/normalized-path)))))
       flatten
       (map str)
       (map filepath->metadata)
       (filter #(not (empty? %)))))

