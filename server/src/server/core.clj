(ns server.core
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer :all]))

(defroutes main-routes
  (PUT "/:client/shows" [client body :as r]
       (println :client client)
       (println :body body)
       {:status 200
        "Content-Type" "text/plain; charset=utf-8"
        :body "ok"})
  (route/not-found "<h1>Page not found</h1>"))

(def app (handler/site main-routes))
