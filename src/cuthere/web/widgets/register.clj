(ns cuthere.web.widgets.register
  (:require [ganelon.web.widgets :as widgets]
            [ganelon.web.actions :as actions]
            [taoensso.timbre :as timbre]
            [cuthere.web.pages.email :refer [register-confirmation-layout]]
            [cuthere.mail :refer [send-mail]]
            [cuthere.db.users :as users]))

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
  (let [user (users/add-user {:email email :username name
                              :password password :type :basic})
        email-confirmation (-> user :email-confirmation :confirmation-text)
        username (user :username)
        body (register-confirmation-layout username email-confirmation)]
    (send-mail [email] "registration" body)
    (user "username")))

