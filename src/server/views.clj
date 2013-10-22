(ns server.views
  (:require [clojure.string :as string]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [unordered-list link-to]]))


(defn share-link [md5 caption]
  (link-to (str "/share/" md5)
           caption))

(defn render-file-sharable [file]
  (share-link (key file)
              (val file)))

(defn render-files [files file-render-fn]
  (for [file files]
    (file-render-fn file)))


(defn files-overview [files]
  (html5 [:h1 "All Files"]
         (unordered-list (render-files files
                                       render-file-sharable))))


(if (= true "hello")
  true)
