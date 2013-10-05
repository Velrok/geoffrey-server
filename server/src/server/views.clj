(ns server.views
  (:require [clojure.string :as string]
            [clojure.pprint :refer [pprint]]))

(defn render-show
  "Converts a map containing show information into a string."
  [show]
  (format "%s - S%02dE%02d"
          (show "showname")
          (show "season#")
          (show "episode#")))

(defn render-shows [shows show-renderer]
  (pprint shows)
  (->> (vals shows)
       flatten
       (map show-renderer)
       set
       vec))
