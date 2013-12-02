(ns cuthere.core
  (:require
    [cuthere.web :refer [handler init]]
    [cuthere.config :refer [get-config get-cli]]
    [ring.middleware.reload :as reload]
    [org.httpkit.server :as http-kit]
    [taoensso.timbre :as timbre]
    [clojure.core.strint :refer [<<]])
  (:gen-class))


(defn start-app [{:keys [dev] :as cli-conf}]
  (let [cfg (get-config cli-conf)
        port (cfg :PORT)]
    (http-kit/run-server
     (if dev (reload/wrap-reload handler) handler)
     {:port port})
    (init cfg)
    (timbre/info (<< "server started on port ~{port}"))))

(defn -main [& args]
  (let [cli (get-cli args)]
    (if (cli :show-help)
      (println (cli :banner))
      (start-app (cli :opts)))))

