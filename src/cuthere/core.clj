(ns cuthere.core
  (:require
    [cuthere.web :refer [handler init]]
    [ring.middleware.reload :as reload]
    [org.httpkit.server :as http-kit]
    [taoensso.timbre :as timbre]
    [clojure.tools.cli :refer [cli]]
    [clojure.core.strint :refer [<<]])
  (:gen-class))


(defn parse-cli [args]
  (cli args
       "CU there app"
       ["-p" "--port" "listen on port" :default 3000 :parse-fn #(Integer. %)]
       ["-d" "--[no-]dev" "start in development mode" :default false]
       ["-h" "--help" "show help" :flag true :default false]))

(defn get-cli [args & {:keys [msg show-help] :or {msg 'nil show-help false}}]
  (try
    (let [[opts extras banner] (parse-cli args)
          banner-extras (if msg (str msg "\n" banner) banner)]
      {:show-help (or show-help (opts :help)), :opts (dissoc opts :help),
       :extras extras, :banner banner-extras})
    (catch Exception e (get-cli [] :msg (.getMessage e) :show-help true))))

(defn start-app [{dev :dev, port :port}]
  (http-kit/run-server
   (if dev (reload/wrap-reload handler) handler)
   {:port port})
  (let [cfg (get-config)]
    (init cfg))
  (timbre/info (<< "server started on port ~{port}")))

(defn get-config []
  {:mongo-uri (System/getenv "MONGOLAB_URI")})

(defn -main [& args]
  (let [cli (get-cli args)]
    (if (cli :show-help)
      (println (cli :banner))
      (start-app (cli :opts)))))
