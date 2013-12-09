(ns cuthere.db.core
  (:require [monger.core :as mg]
            [taoensso.timbre :as timbre]
            [clojure.core.strint :refer [<<]]))


(defn init [uri]
  (timbre/info (<< "db: ~{uri}"))
  (mg/connect-via-uri! uri))
