(ns cuthere.web.handlers.registration
  (:require [taoensso.timbre :as timbre]
            [clojure.core.strint :refer [<<]]
            [monger.collection :as mc]
            [monger.operators :refer [$set]]
            [cuthere.web.pages.email :refer [register-confirmation-layout]]
            [cuthere.mail :refer [send-mail]]
            [cuthere.db.users :as users]))


(defn confirm-user [confirmation-text]
  (when-let [user (mc/find-one-as-map "users" {"email-confirmation.confirmation-text"
                                             confirmation-text})]
    (do
      (mc/update "users" {:_id (user :_id)}
                 {$set {"email-confirmation.confirmed" true}})
      (timbre/info (<< "user ~{(user :username)} confirmed"))
      true)))

(defn register-user [email name password]
  (let [user (users/add-user {:email email :username name
                              :password password :type :basic})
        email-confirmation (-> user :email-confirmation :confirmation-text)
        username (user :username)
        body (register-confirmation-layout username email-confirmation)]
    (send-mail [email] "registration" body)
    (user "username")))
