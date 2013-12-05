(ns cuthere.web.pages.email
  (:require [hiccup.page :as hiccup]
            [hiccup.core :as h]))


(defn email-layout [& content]
  (hiccup/html5
   content))

(defn register-confirmation-layout [username email-confirmation]
  (email-layout
   (h/html
    [:p username]
    [:p email-confirmation])))
