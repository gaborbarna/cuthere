(ns cuthere.web.pages.common
  (:require [cuthere.web.widgets.register :refer [register-widget]]
            [hiccup.page :as hiccup]
            [hiccup.core :as h]
            [hiccup.form :refer
             [form-to submit-button text-field password-field label]]
            [noir.response :as resp]))


(def login-form
  (h/html
   (form-to [:post "/login"]
            (label "login" "Login")
            (text-field "username")
            (password-field "password")
            (submit-button "login"))))

(defn navbar []
  (h/html
    [:div.navbar.navbar-fixed-top {:style "opacity: 0.9;"}
      [:div.container
       [:a.brand {:href "/"} "CUthere"]
       login-form]]))

(defn footer []
  (h/html
   [:footer {:style "text-align: center; padding: 30px 0; margin-top: 70px;"}
    [:div.container [:p "CUthere by Gabor Barna"]]]))


(defn layout [& content]
  (hiccup/html5
    [:head [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:title "CUthere"]
     (hiccup/include-css "/ganelon/css/bootstrap.css")
     (hiccup/include-css "/ganelon/css/bootstrap-responsive.css")
     (hiccup/include-css "/ganelon/css/jquery.gritter.css")
     (hiccup/include-css "/datepicker/css/datepicker.css")
     (hiccup/include-css "/timepicker/css/bootstrap-timepicker.min.css")
     (hiccup/include-js "/ganelon/js/jquery-1.8.1.min.js") ;jQuery - required
     (hiccup/include-js "/ganelon/js/bootstrap.js") ;Bootstrap - optional
     (hiccup/include-js "/ganelon/js/ganelon.js") ;basic actions support
     (hiccup/include-js "/ganelon/js/ext/ganelon.ops.bootstrap.js") ;additional Bootstrap related actions
     (hiccup/include-js "/ganelon/js/ext/ganelon.ops.gritter.js") ; growl-style notifications through gritter.js
     (hiccup/include-js "/ganelon/actions.js") ;dynamic actions interface
     (hiccup/include-js "/js/ganelon-tutorial.js") ;additional plugins
     (hiccup/include-js "/datepicker/js/bootstrap-datepicker.js") ;date picker
     (hiccup/include-js "/timepicker/js/bootstrap-timepicker.min.js") ;date picker
     ;code highlighting
     (hiccup/include-css "http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.css")
     (hiccup/include-js "http://google-code-prettify.googlecode.com/svn/trunk/src/prettify.js")
     (hiccup/include-js "http://google-code-prettify.googlecode.com/svn/trunk/src/lang-css.js")
     (hiccup/include-js "http://google-code-prettify.googlecode.com/svn/trunk/src/lang-clj.js")
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

(defn register-layout [& content]
  (layout
   (register-widget)))

(defn confirmation-layout [confirmed & content]
  (layout
   (h/html
    (if confirmed [:p "Confirmation successful"] [:p "Confirmation error"]))))
