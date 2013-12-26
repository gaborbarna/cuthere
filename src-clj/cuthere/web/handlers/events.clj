(ns cuthere.web.handlers.events
  (:require [taoensso.timbre :as timbre]
            [cuthere.utils :refer [dbg]]
            [monger.query :as mq]
            [monger.collection :as mc]
            [clojure.tools.trace :refer [deftrace]]
            [clojure.core.strint :refer [<<]]
            [cuthere.db.events :as db-events])
  (:import [org.bson.types ObjectId]))


(deftrace create-event [user event]
  (let [event (db-events/create-event user event)
        event-id (event :_id)]
    (timbre/info (<< "event created ~{event-id}"))
    {:success true}))

(deftrace list-events []
  (let [events (mq/with-collection "events"
                 (mq/find {})
                 (mq/fields [:name]))]
    (map (fn [e] (update-in e [:_id] #(.toString %))) events)))

(deftrace attend-event [user event-id]
  (mc/update "events" {:_id (ObjectId. event-id)}
             {"$addToSet" {:going (user :_id)}})
  {:success true})
