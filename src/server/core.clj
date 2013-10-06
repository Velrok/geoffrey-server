(ns server.core
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.page :refer [html5]]
            [hiccup.element :refer [unordered-list]]
            [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]
            [clojure.set :refer [difference]]
            [ring.middleware.json :refer :all]
            [compojure.core :refer :all]
            [server.views :as views]))

(def shows (atom {}))

(def inboxes (atom {}))

(defn update-shows!
  [data-base client data]
  (swap! data-base
         assoc
         client
         data)
  @data-base)

(defn select-episodes
  [shows showname season episode]
  (->> shows
       vals
       flatten
       (filter #(= showname
                   (% "showname")))
       (filter #(= season
                   (% "season#")))
       (filter #(= episode
                   (% "episode#")))))

(defn contains-episode?
  [shows showname season episode]
  (->> (select-episodes shows showname season episode)
       empty?
       not))


(defn create-inbox [client]
  (when-not (contains? @inboxes client)
    (swap! inboxes #(assoc % client #{}))))

(defn overview [shows]
  (html5 [:h1 "hello world"]
         (unordered-list (views/render-shows shows
                                             views/render-show))))
(defrecord Message [type content])

(defn broadcast! [inbox message]
  (println "boradcasting message" message)
  (doseq [client (keys @inbox)]
    (pprint client)
    (swap! inbox
           (fn [m]
             (update-in m [client] #(conj % message))))))

(defn share [showname season episode]
  (if (contains-episode? @shows
                         showname
                         season
                         episode)
    (do
      (let [found (first (select-episodes @shows
                                   showname
                                   season
                                   episode))]
        (broadcast! inboxes (->Message :share
                                       found))
        true))
    false))

(defroutes main-routes
  (GET "/" []
       (overview @shows))
  (GET "/debug/inboxes" []
       (json/write-str @inboxes))
  (GET "/share/:showname/:season/:episode"
       [showname season episode]
       (let [s (Integer/parseInt season)
             e (Integer/parseInt episode)]
        (if (share showname s e)
          (format "Successfully shared %s S%02dE%02d"
                  showname
                  s
                  e)
          {:status 404
            :body (format "Can't find anyone who has %s S%02dE%02d"
                      showname
                      s
                      e)})))
  (PUT "/:client/shows" [client :as r]
       (update-shows! shows
                      client
                      (->> r
                           :body
                           json/read-str))
       (create-inbox client)
       {:status 200})
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
