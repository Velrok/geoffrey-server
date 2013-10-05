(ns server.core
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [unordered-list]]
            [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]
            [ring.middleware.json :refer :all]
            [compojure.core :refer :all]
            [server.views :as views]))

(def shows (atom {}))

(defn update-shows!
  [data-base client data]
  (swap! data-base
         assoc
         client
         data)
  @data-base)



(defn overview [shows]
  (html5 [:h1 "hello world"]
         (unordered-list (views/render-shows shows
                                             views/render-show))))

(defroutes main-routes
  (GET "/" []
       (overview @shows))
  (PUT "/:client/shows" [client :as r]
       (update-shows! shows
                      client
                      (->> r
                           :body
                           json/read-str))
       {:status 200})
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> main-routes
      wrap-json-body
      handler/site))
