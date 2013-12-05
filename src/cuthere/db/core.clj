(ns cuthere.db.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [taoensso.timbre :as timbre]
            [cemerick.friend.credentials :refer [hash-bcrypt]]
            [clojure.core.strint :refer [<<]]
            [clojure.tools.trace :refer [deftrace]]
            [crypto.random])
  (:import [org.bson.types ObjectId]))


(defn init [uri]
  (timbre/info (<< "db: ~{uri}"))
  (mg/connect-via-uri! uri))

(defn create-user-map [username password type]
  (let [hash-pwd (hash-bcrypt password)
        user {:_id (ObjectId.) :username username :password hash-pwd
              :type type}]
    (cond (= type :basic)
          (let [verification-text (crypto.random/url-part 32)]
            (assoc user
              :email-verification {:verification-text verification-text,
                                   :verified false}))
          :else user)))

(defn add-user [email username hash-pwd type]
  (let [user-map (create-user-map username hash-pwd type)]
    (mc/insert-and-return
     "users" user-map)))

(defn verify-user [username verification-text]
  (if-let [user (mc/find-one-as-map "users" {:username username})]
    (if (= (user :type) :basic)
      (= ((user :email-verification) :verification-text) verification-text)
      true)
    false))

(defn get-dbobject-fields [dbobject & fields]
  (into {} (for [k fields] [(keyword k) (.get dbobject k)])))

(defn load-user-record [username]
  (timbre/info (<< "looking up user \"~{username}\""))
  (if-let [user (mc/find-one "users" {:username username})]
    (get-dbobject-fields user "username" "password")
    nil))

