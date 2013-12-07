(ns cuthere.web.handlers.registration
  (:require [clojure.core.strint :refer [<<]]
            [taoensso.timbre :as timbre]
            [clojure.core.strint :refer [<<]]
            [monger.collection :as mc]
            [monger.operators :refer [$set]]))


(defn confirm-user [confirmation-text]
  (if-let [user (mc/find-one-as-map "users" {"email-confirmation.confirmation-text"
                                             confirmation-text})]
    (do
      (mc/update "users" {:_id (user :_id)}
                 {$set {"email-confirmation.confirmed" true}})
      (timbre/info (<< "user ~{(user :username)} confirmed"))
      true)
    false))
