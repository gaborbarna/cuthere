(ns cuthere.db.users
  (:require [cemerick.friend.credentials :refer [hash-bcrypt]]
            [monger.collection :as mc]
            [taoensso.timbre :as timbre]
            [clojure.core.strint :refer [<<]]
            [cuthere.utils :refer [dbg]]
            [crypto.random])
    (:import [org.bson.types ObjectId]))


(defn confirmed? [user]
  (or (not= (user :type) "basic") (-> user :email-confirmation :confirmed)))

(defn load-user-record [username & {:keys [type] :or {type :basic}}]
  (timbre/info (<< "looking up user \"~{username}\""))
  (when-let [user (mc/find-one-as-map "users" {:username username
                                               :type type})]
    (when (confirmed? user) user)))

(defn store-user [user-map]
  (let [username (user-map :username)]
    (timbre/info (<< "creating user ~{username}"))
    (mc/insert-and-return "users" (assoc user-map :_id (ObjectId.)))))

(defmulti add-user :type)

(defmethod add-user :basic [user-map]
  (let [confirmation-text (crypto.random/url-part 32)]
    (-> user-map
        (update-in [:password] hash-bcrypt)
        (assoc :email-confirmation {:confirmation-text confirmation-text,
                                    :confirmed false})
        (store-user))))

(defmethod add-user :facebook [user-map]
  (store-user user-map))

(defn get-facebook-user [body]
  (if-let [user (load-user-record (body "username") :type :facebook)]
    user
    (let [keyword-map (into {} (for [[k v] body] [(keyword k) v]))]
      (add-user (assoc keyword-map :type :facebook)))))
