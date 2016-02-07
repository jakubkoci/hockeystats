(defproject hockeystats "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/data.csv "0.1.3"]]
  :source-paths ["src/clj"]
  :plugins [[lein-cljsbuild "1.1.2"]]
  :cljsbuild {:builds [{
                         :source-paths ["src/cljs"]
                         :compiler {
                                     :output-to "resources/public/app.js"
                                     :optimizations :whitespace
                                     :pretty-print true}}]})
