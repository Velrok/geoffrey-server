(ns server.core-spec
  (:require [server.core :refer :all]
            [speclj.core :refer :all]))

(describe "validate-files"
  (it "returns true if the files data is valid"
    (should (validate-files [{"md5-hash1" "filename1"}
                             {"md5-hash2" "filename2"}])))
  (it "returns an error string stating that a list is required if not given."
    (should (re-matches #".*must.*vector.*"
                        (validate-files {:hello :filename}))))
  (it "returns an error string stating that the list must contain objects if not given."
    (should (re-matches #".*must.*contain.*object.*"
                        (validate-files ["hello" "asdf"])))))


(describe "reset-clients-files!"
  (with test-files (atom {:c1 [{:hash :filename
                                :hash2 :filename2}]
                          :c2 [{:md5 :filename-md5}]}))
  (before (reset-clients-files! @test-files :c1 []))

  (it "resets the given client files"
    (should= []
             (:c1 @@test-files)))
  (it "keeps the other clients untouched"
    (should= [{:md5 :filename-md5}]
             (:c2 @@test-files))))
