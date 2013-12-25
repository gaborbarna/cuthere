(ns cuthere.web
  (:gen-class)
  (:require [cuthere.db.core :as db]
            [cuthere.web.middleware :refer [wrap-friend wrap-only-identified]]
            [cuthere.web.routes :as routes]
            [ring.middleware.stacktrace]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.reload]
            [ring.middleware.json :as json]
            [noir.util.middleware :as middleware]
            [taoensso.timbre :as timbre]
            [noir.session :as sess]))


(defn init [cfg]
  (timbre/info "initialize app")
  (db/init (or (cfg :MONGOLAB_URI) (cfg :mongo-uri))))

(defn destroy []
  (timbre/info "cuthere destroy"))

(def handler (middleware/app-handler
              [routes/basic
               (-> routes/only-identified (wrap-resource "public") wrap-only-identified)]
              :middleware [wrap-friend json/wrap-json-response json/wrap-json-params
                           ring.middleware.stacktrace/wrap-stacktrace
                           #(wrap-resource % "public")
                           #(ring.middleware.reload/wrap-reload % {:dirs ["src-clj/cuthere"]})]
              :access-rules []
              ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
              :formats [:json-kw :edn]))
