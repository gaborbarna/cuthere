(ns cuthere.web.oauth
  (:require [clj-oauth2.client :as oauth2]))


(defn get-facebook-oauth2 [cfg]
  {:authorization-uri "https://graph.facebook.com/oauth/authorize"
   :access-token-uri "https://graph.facebook.com/oauth/access_token"
   :redirect-uri "http://cuthere.herokuapp.com/facebook-callback"
   :client-id (cfg :FACEBOOK_CLIENT_ID)
   :client-secret (cfg :FACEBOOK_CLIENT_SECRET)
   :access-query-param :access_token
   :scope ["user_events"]
   :grant-type "authorization_code"})

(defn get-auth-req [cfg]
  (oauth2/make-auth-request
   (get-facebook-oauth2 cfg)
   "some-csrf-protection-string"))
