(ns cuthere.repl
  (:require [cuthere.web :refer [handler init destroy]]
            [cuthere.db.core :as db]
            [ring.server.standalone :refer [serve]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [clojure.edn :as edn]
            [clojure.core.strint :refer [<<]]))


(defonce server (atom nil))

(defn get-handler []
  (-> #'handler
    (wrap-file "resources")
    (wrap-file-info)))

(defn start-server
  [& [port config-file]]
  (let [file-conf (edn/read-string (slurp (or config-file "config/default.edn")))
        port (file-conf :PORT)]
    (reset! server
            (serve (get-handler)
                   {:port port
                    :init #(init file-conf)
                    :auto-reload? true
                    :destroy destroy
                    :join? false}))
    (println (<< "web app at http://localhost:~{port}"))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn add-user [email username password]
  (let [user (db/add-user email username password :custom)]
    (println user)))
