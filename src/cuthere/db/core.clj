(ns cuthere.db.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [taoensso.timbre :as timbre]
            [cemerick.friend.credentials :refer [hash-bcrypt]]
            [clojure.core.strint :refer [<<]])
  (:import [org.bson.types ObjectId]))


(defn init
  [& {:keys [host port db] :or {host "localhost", port 27017, db "cuthere"}}]
  (mg/connect! {:host host :port port})
  (mg/set-db! (mg/get-db "cuthere"))
  (timbre/info (<< "db: ~{host}:~{port}/~{db}")))

(defn add-user [username password]
  (let [hash-pwd (hash-bcrypt password)]
    (mc/insert-and-return
     "users" {:_id (ObjectId.) :username username :password hash-pwd})))

(defn get-dbobject-fields [dbobject & fields]
  (into {} (for [k fields] [(keyword k) (.get dbobject k)])))

(defn load-user-record [username]
  (timbre/info (<< "looking up user \"~{username}\""))
  (if-let [user (mc/find-one "users" {:username username})]
    (get-dbobject-fields user "username" "password")
    nil))

