(ns client.core-spec
  (:require [client.core :refer :all]
            [speclj.core :refer :all]))

(describe "filepath->metadata"
  (with castle-mkv "Castle (2009)/Castle (2009) - 5x01 - After The Storm.mkv")
  (with castle-banner "Castle (2009)/banner.jpg")
  (with true-bood-nfo "/True Blood/Season 06/True Blood - S06E05 - Let's Boot and Rally - HD TV.nfo")

  (it "finds the season number"
    (should= 5 (:season# (filepath->metadata @castle-mkv)))
    (should= 6 (:season# (filepath->metadata @true-bood-nfo))))
  (it "finds the episode number"
    (should= 1 (:episode# (filepath->metadata @castle-mkv)))
    (should= 5 (:episode# (filepath->metadata @true-bood-nfo))))
  (it "finds the showname"
    (should= "Castle (2009)" (:showname (filepath->metadata @castle-mkv)))
    (should= "True Blood"    (:showname (filepath->metadata @true-bood-nfo))))
  (it "finds the title"
    (should= "After The Storm"      (:title (filepath->metadata @castle-mkv)))
    (should= "Let's Boot and Rally" (:title (filepath->metadata @true-bood-nfo))))
  (it "contains the full filename"
    (should= @castle-mkv    (:filepath (filepath->metadata @castle-mkv)))
    (should= @true-bood-nfo (:filepath (filepath->metadata @true-bood-nfo))))
  (it "containd the file extention"
    (should= "mkv" (:ext (filepath->metadata @castle-mkv)))
    (should= "nfo" (:ext (filepath->metadata @true-bood-nfo))))
  (it "returns empty map in case data can not be parsed"
    (should= {}
             (filepath->metadata @castle-banner))))
