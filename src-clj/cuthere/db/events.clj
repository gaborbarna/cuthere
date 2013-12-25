(ns cuthere.db.events
  (:require [monger.collection :as mc]
            [taoensso.timbre :as timbre]
            [clojure.tools.trace :refer [deftrace]]
            [clojure.core.strint :refer [<<]])
    (:import [org.bson.types ObjectId]))


(deftrace create-event [user event]
  (let [id (user :_id)]
    (mc/insert-and-return "events" (conj event {:_id (ObjectId.), :owner id}))))

