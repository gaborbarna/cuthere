(ns cuthere.web.pages.common
  (:require [ganelon.web.dyna-routes :as dyna-routes]
            [ganelon.web.widgets :as widgets]
            [ganelon.web.actions :as actions]
            [ganelon.web.ui-operations :as ui]
            [ganelon.web.helpers :as webhelpers]
            [hiccup.page :as hiccup]
            [hiccup.core :as h]
            [hiccup.form :refer
             [form-to submit-button text-field password-field label]]
            [hiccup.util]
            [noir.response :as resp]
            [noir.request :as req]
            [noir.session :as sess]
            [compojure.core :as compojure]))

(defn navbar []
  (h/html
    [:div.navbar.navbar-fixed-top {:style "opacity: 0.9;"}
      [:div.container
       [:a.brand {:href "/"} "CUthere"]]]))

(defn footer []
  (h/html
   [:footer {:style "text-align: center; padding: 30px 0; margin-top: 70px;"}
    [:div.container [:p "CUthere by Gabor Barna"]]]))


(defn layout [& content]
  (hiccup/html5
    [:head [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:title "CUthere"]
     ;real life site should use CDN/minify to serve static resources
     (hiccup/include-css "/ganelon/css/bootstrap.css")
     (hiccup/include-css "/ganelon/css/bootstrap-responsive.css")
     ]
    [:body.default-body
     [:div#navbar (navbar)]
     [:div.container {:style "padding-top: 70px"} content]
     (footer)]))

(defn home-layout [& contents]
  (layout
   (h/html
    [:p "home"]
    [:p contents])))

(defn login-layout [& content]
  (layout
   (h/html
    (form-to [:post "/login"]
      (label "login" "Login")
      (text-field "username")
      (password-field "password")
      (submit-button "login"))
    [:p content])))
