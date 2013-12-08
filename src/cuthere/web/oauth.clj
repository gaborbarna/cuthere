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

(defn get-auth-req [cfg csrf]
  (oauth2/make-auth-request
   (get-facebook-oauth2 cfg)
   csrf))

(defn get-access-token [cfg csrf auth-resp]
  (oauth2/get-access-token
   (get-facebook-oauth2 cfg) auth-resp (get-auth-req cfg csrf)))

(defn get-facebook-me [acces-token]
  (oauth2/get "https://graph.facebook.com/me" access-token))
