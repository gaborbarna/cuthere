(ns cuthere.web.middleware
  (:require [cuthere.db.core :refer [load-user-record]]
            [cuthere.web.misc :refer [redirect-to-login]]
            [cuthere.config :refer [cfg]]
            [cuthere.utils :refer [dbg]]
            [cuthere.web.oauth :refer [get-auth-req get-access-token
                                       get-facebook-me]]
            [ring.util.response :as resp]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :refer [make-auth]]
            [compojure.core :refer [GET POST routes defroutes]]
            [crypto.random]
            [cemerick.friend [workflows :as workflows]
                             [credentials :as creds]]))


(defn custom-workflow []
  (routes
   (GET "/logout" req
        (friend/logout* (resp/redirect (str (:context req) "/"))))
   (POST "/basic-login" {:keys [params session] :as request}
         (if-let [user-record (-> params :username load-user-record)]
           (if (creds/bcrypt-verify (params :password) (:password user-record))
             (make-auth (dissoc user-record :password)
                        {::friend/workflow :multi-factor
                         ::friend/redirect-on-auth? true})
             (redirect-to-login request))
           (redirect-to-login request)))
   (GET "/facebook-login" {:keys [params session] :as request}
        (let [csrf (crypto.random/url-part 64)
              auth-req (get-auth-req cfg csrf)]
          (assoc (resp/redirect (auth-req :uri)) :session (assoc session :csrf csrf))))
   (GET "/facebook-callback" {:keys [params session] :as request}
        (if (= (session :csrf) (params :state))
          (do
            (dbg "OK")
            (let [access-token (get-access-token cfg (session :csrf) params)
                  me (get-facebook-me {:oauth2 access-token})]
              (dbg access-token)
              (dbg me)
              (redirect-to-login request)))
              (redirect-to-login request)))))

(defn wrap-friend [handler]
  "Wrap friend authentication around handler."
  (friend/authenticate
   handler
   {:allow-anon? true
    :login-uri "/login"
    :default-landing-uri "/"
    :unauthorized-handler #(-> "unauthorized"
                               resp/response
                               (resp/status 401))
    :workflows [(custom-workflow)]}))
