(ns cuthere.web.widgets.register
  (:require [ganelon.web.widgets :as widgets]
            [ganelon.web.actions :as actions]
            [cuthere.db.core :as db]))

(defn register-widget []
  (widgets/with-div
    [:h1 "Registration"]
    (widgets/action-form
     "register" {} {}
     [:input#inputTitle {:type "text" :name "name" :required "1"}]
     [:input#inputTitle {:type "password" :name "password" :required "1"}]
     [:button {:type "submit"} "Register"])))
     
(actions/defjsonaction "register" [name password]
  (let [user (db/add-user name password)]
    (.get user "username")))

