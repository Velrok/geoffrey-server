(ns server.messaging
  (:require [clojure.pprint :refer [pprint]]))

(defrecord Message [type content])

(defn create-inbox [inboxed-atom client]
  (when-not (contains? @inboxed-atom client)
    (swap! inboxed-atom #(assoc % client #{}))))

(defn broadcast! [inbox message]
  (println "boradcasting message" message)
  (doseq [client (keys @inbox)]
    (pprint client)
    (swap! inbox
           (fn [m]
             (update-in m [client] #(conj % message))))))
