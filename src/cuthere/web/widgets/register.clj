(ns cuthere.web.widgets.register
  (:require [ganelon.web.widgets :as widgets]
            [ganelon.web.actions :as actions]
            [taoensso.timbre :as timbre]
            [cuthere.web.pages.email :refer [register-confirmation-layout]]
            [cuthere.mail :refer [send-mail]]
            [cuthere.db.core :as db]))

(defn register-widget []
  (widgets/with-div
    [:h1 "Registration"]
    (widgets/action-form
     "register" {} {}
     [:input#inputTitle {:type "text" :name "name" :required "1"}]
     [:input#inputTitle {:type "text" :name "email" :required "1"}]
     [:input#inputTitle {:type "password" :name "password" :required "1"}]
     [:button {:type "submit"} "Register"])))
     
(actions/defjsonaction "register" [email name password]
  (let [user (db/add-user email name password :basic)
        email-verification (-> user :email-verification :verification-text)
        username (user :username)
        body (register-confirmation-layout username email-verification)]
    (send-mail [email] "registration" body)
    (user "username")))

