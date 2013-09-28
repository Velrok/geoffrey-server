(ns client.core-spec
  (:require [client.core :refer :all]
            [speclj.core :refer :all]))

(describe "filepath->metadata"
  (with castle-mkv "Castle (2009)/Castle (2009) - 5x01 - After The Storm.mkv")
  (with castle-banner "Castle (2009)/banner.jpg")
  (with true-bood-nfo "/True Blood/Season 06/True Blood - S06E05 - Let's Boot and Rally - HD TV.nfo")
  (with existing-file "./fixtures/fs/shows/True Blood/Season 05/True Blood - 5x04 - We'll Meet Again - HD TV.mkv")

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
             (filepath->metadata @castle-banner)))
  (it "contains the md5 hash of the file contents"
    (should-not= nil
                 (:md5 (filepath->metadata @existing-file))))
  (it "contains nil for md5 in case the file is missing"
    (should= nil
             (:md5 (filepath->metadata @castle-mkv)))))

(describe "list-media-files"
  (with shows-dir "./fixtures/fs/shows")
  (with castle-files (filter (fn [{n :showname}]
                               (= n "Castle (2009)"))
                             (list-media-files @shows-dir)))

  (with true-blood-files (filter (fn [{n :showname}]
                                   (= n "True Blood"))
                                 (list-media-files @shows-dir)))

  (with true-blood-mkvs (filter (fn [{e :ext}]
                                   (= e "mkv"))
                                @true-blood-files))

  (with true-blood-nfos (filter (fn [{e :ext}]
                                   (= e "nfo"))
                                @true-blood-files))

  (it "finds the Castle mkv file"
    (should= 1 (count @castle-files))
    (should= "After The Storm"
             (:title (first @castle-files))))
  (it "finds all True Blood mkv files"
    (should= 4 (count @true-blood-mkvs)))

  (it "finds all True Blood nfo files"
    (should= 4 (count @true-blood-nfos))))




