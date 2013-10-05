(ns server.views-spec
  (:require [speclj.core :refer :all]
            [server.views :refer :all]))

(describe "render-show"
  (with show {"showname" "True Blood"
              "title"    "Kill em all!"
              "season#"  5
              "episode#" 2})
  (it "converts a show map into a string containing showname, season and episode"
    (should= "True Blood - S05E02"
             (render-show @show))))

(describe "render-shows"
  (with shows {"timmy" [{"showname" "True Blood"
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
  (it "takes a map indext by client as well as a show renderer and turns a hiccup unordered list"
    (should= ["True Blood - S05E02" "True Blood - S05E03"]
             (render-shows @shows render-show)))
  (it "is robust agains an empty map"
    (should= []
             (render-shows {} render-show))))
