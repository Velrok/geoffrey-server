(ns server.core-spec
  (:require [server.core :refer :all]
            [speclj.core :refer :all]))

(describe "update-shows!"
  (with db (atom {}))
  (it "update shows for a client in the shows atom (assumed to contain a map, indexed by client)"
    (should= {"client1" :test-data}
             (update-shows! @db "client1"
                            :test-data))))

(describe "contains-episode?"
  (with test-shows {"timmy" [{"showname" "True Blood"
                              "title"    "Kill em all!"
                              "season#"  5
                              "episode#" 2}
                             {"showname" "True Blood"
                              "title"    "Ferys suck"
                              "season#"  5
                              "episode#" 3}]
                    "jonny" [{"showname" "True Blood"
                              "title"    "Kill em all!"
                              "season#"  5
                              "episode#" 2}]})
  (it "returns true if at least one client has the requested episode"
    (should (contains-episode?
              @test-shows "True Blood" 05 02))
    (should (contains-episode?
              @test-shows "True Blood" 5 3)))
  (it "returns fase if none of the clients has the requested episode"
    (should-not (contains-episode? @test-shows "hello kitty" 1 2))
    (should-not (contains-episode? @test-shows "True Blood" 5 4))
    (should-not (contains-episode? @test-shows "True Blood" 4 2))))

(describe "select-episodes"
  (with test-shows {"timmy" [{"showname" "True Blood"
                              "title"    "Kill em all!"
                              "season#"  5
                              "episode#" 2}
                             {"showname" "True Blood"
                              "title"    "Ferys suck"
                              "season#"  5
                              "episode#" 3}]
                    "jonny" [{"showname" "True Blood"
                              "title"    ""
                              "season#"  5
                              "episode#" 2}]})
  (it "returns all the matching episodes"
    (should= [{"season#" 5, "episode#" 3, "showname" "True Blood", "title" "Ferys suck"}]
             (select-episodes @test-shows "True Blood" 5 3))
    (should= [{"season#" 5, "episode#" 2, "showname" "True Blood", "title" "Kill em all!"}
              {"season#" 5, "episode#" 2, "showname" "True Blood", "title" ""}]
             (select-episodes @test-shows "True Blood" 5 2)))
  (it "returns an empty seq in case nothing matches"
    (should= ()
             (select-episodes @test-shows "rainbows" 1 2))))


(describe "broadcast!"
  (with ib (atom {"tom"   #{}
                  "jerry" #{}}))
  (with msg (->Message :greet "hello"))
  (before (broadcast! @ib @msg))
  (it "appends a message to all clients"
    (should= {"tom"   #{@msg}
              "jerry" #{@msg}}
             @@ib)))
