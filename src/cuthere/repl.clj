(ns cuthere.repl
  (:require [cuthere.web :refer [handler init destroy]]
            [cuthere.db.core :as db]
            [ring.server.standalone :refer [serve]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [clojure.core.strint :refer [<<]]))


(defonce server (atom nil))

(defn get-handler []
  (-> #'handler
    (wrap-file "resources")
    (wrap-file-info)))

(defn start-server
  [& [port]]
  (let [port (if port (Integer/parseInt port) 3000)]
    (reset! server
            (serve (get-handler)
                   {:port port
                    :init init
                    :auto-reload? true
                    :destroy destroy
                    :join? false}))
    (println (<< "web app at http://localhost:~{port}"))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

(defn add-user [username password]
  (let [user (db/add-user username password)]
    (println user)))
