(defproject server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 ;[digest "1.4.3"]
                 ;[environ "0.4.0"]
                 ;[clj-http "0.7.7"]
                 [compojure "1.1.5"]
                 [org.clojure/data.json "0.2.3"]
                 ;[org.clojure/core.async "0.1.222.0-83d0c2-alpha"]
                 [speclj "2.7.4"]]
  :plugins [[speclj "2.5.0"]
            [lein-ring "0.7.1"]]
  :main server.core
  :ring {:handler server.core/app}
  :test-paths ["spec"])
