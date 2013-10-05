(ns server.views
  (:require [clojure.string :as string]
            [hiccup.element :refer [link-to]]
            [hiccup.util :refer [url url-encode]]
            [clojure.pprint :refer [pprint]]))

(defn share-show-url
  [show]
  (format "/share/%s/%d/%d"
          (string/replace (show "showname")
                          #" "
                          "%20")
          (show "season#")
          (show "episode#")))

(defn show->str [show]
  (format "%s S%02dE%02d - %s"
          (show "showname")
          (show "season#")
          (show "episode#")
          (show "title")))

(defn render-show
  "Converts a map containing show information into a string."
  [show]
  (let [showname (show "showname")
        season   (show "season#")
        episode  (show "episode#")
        link-name (show->str show)]
    (link-to (share-show-url show)
             link-name)))

(defn render-shows [shows show-renderer]
  (->> (vals shows)
       flatten
       (map show-renderer)
       set
       vec))
