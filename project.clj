(defproject cuthere "0.1.0-SNAPSHOT"
  :description "CU there app"
  :url "http://cuthe.re"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-json "0.5.0"]
                 [ganelon "0.9.0"]
                 [http-kit "2.1.13"]
                 [com.taoensso/timbre "2.7.1"]
                 [org.clojure/core.incubator "0.1.3"]
                 [org.clojure/tools.cli "0.2.4"]
                 [com.cemerick/friend "0.2.0"]
                 [com.novemberain/monger "1.5.0"]
                 [ring-server "0.3.1"]
                 [ritz/ritz-nrepl-middleware "0.7.0"]
                 [org.clojure/tools.trace "0.7.6"]
                 [com.draines/postal "1.11.1"]
                 [wuzhe/clj-oauth2 "0.5.3"]
                 [crypto-random "1.1.0"]]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :main cuthere.core
  :plugins [[lein-ring "0.8.6"]
            [lein-ritz "0.7.0"]]
  :repl-options {:nrepl-middleware
                 [ritz.nrepl.middleware.javadoc/wrap-javadoc
                  ritz.nrepl.middleware.simple-complete/wrap-simple-complete]}
  :ring {:handler cuthere.web/handler :init cuthere.web/init})
