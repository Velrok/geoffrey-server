(ns server.views-spec
  (:require [speclj.core :refer :all]
            [server.views :refer :all]))

(describe "show->str"
  (with show {"showname" "True Blood"
              "title"    "Kill em all!"
              "season#"  5
              "episode#" 2})
  (it "converts a show map into a string"
    (should= "True Blood S05E02 - Kill em all!"
             (show->str @show))))

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
    (should= 2
             (count (render-shows @shows render-show))))
  (it "is robust agains an empty map"
    (should= []
             (render-shows {} render-show))))

(describe "share-show-url"
  (with show {"showname" "True Blood"
                         "title"    "Kill em all!"
                         "season#"  5
                         "episode#" 2})
  (it "returns the url as a relative path"
    (should= "/share/True%20Blood/5/2"
             (share-show-url @show))))
