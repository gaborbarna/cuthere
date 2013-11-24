(ns cuthere.web.middleware
  (:require [cuthere.db.core :refer [load-user-record]]
            [ring.util.response :as resp]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))


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
    :credential-fn #(creds/bcrypt-credential-fn load-user-record %)
    :workflows [(workflows/interactive-form)]}))
