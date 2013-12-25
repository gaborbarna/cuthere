(ns cuthere.mail
  (:require
   [cuthere.config :refer [cfg]]
   [taoensso.timbre :as timbre]
   [clojure.core.strint :refer [<<]]
   [postal.core :refer [send-message]])
  (:gen-class))

(defn send-mail [to subject body]
  (future
    (send-message {:from (cfg :email-noreply)
                   :to to
                   :subject subject
                   :body body})
    (timbre/info (<< "mail sent to ~{to}"))))
