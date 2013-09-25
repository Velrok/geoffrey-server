(defproject client "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [me.raynes/fs "1.4.4"]
                 [speclj "2.7.4"]]
  :plugins [[speclj "2.5.0"]]
  :main client.core
  :test-paths ["spec"])
