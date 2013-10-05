(ns server.core-spec
  (:require [server.core :refer :all]
            [speclj.core :refer :all]))

(describe "update-shows!"
  (with db (atom {}))
  (it "update shows for a client in the shows atom (assumed to contain a map, indexed by client)"
    (should= {"client1" :test-data}
             (update-shows! @db "client1"
                            :test-data))))
