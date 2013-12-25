(ns cuthere.web.routes
  (:require [cuthere.web.pages.common :as common]
            [cuthere.web.middleware :refer [wrap-friend]]
            [cuthere.utils :refer [dbg]]
            [cuthere.web.handlers.events :as events]
            [cemerick.friend :as friend]
            [ring.util.response :as resp]
            [cuthere.web.handlers.registration :refer [confirm-user]]
            [compojure.route :refer [resources]]
            [compojure.core :refer [GET POST ANY defroutes]]))


(defn confirm [confirmation-text]
  (let [confirmed? (confirm-user confirmation-text)]
    (common/confirmation-layout confirmed?)))

(defroutes basic
  (GET "/" req (common/home-layout req))
  (GET "/login" req (common/login-layout req))
  (GET "/logout" req (friend/logout* (resp/redirect (str (:context req) "/"))))
  (GET "/register" req (common/register-layout req))
  (GET "/confirmation/:confirmation-text" [confirmation-text & rest] (confirm confirmation-text))
  (GET "/list-events" req (resp/response (events/list-events))))

(defroutes only-identified
  (GET "/create-event" req (common/create-event-layout req))
  (POST "/create-event" [user event :as req]
        (resp/response (events/create-event user event))))
