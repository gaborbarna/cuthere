(ns cuthere.web.handlers.events
  (:require [taoensso.timbre :as timbre]
            [monger.query :as mq]
            [clojure.tools.trace :refer [deftrace]]
            [clojure.core.strint :refer [<<]]
            [cuthere.db.events :as db-events]))


(deftrace create-event [user event]
  (let [event (db-events/create-event user event)
        event-id (event :_id)]
    (timbre/info (<< "event created ~{event-id}"))
    {:success true}))

(deftrace list-events []
  (mq/with-collection "events"
    (mq/find {})
    (mq/fields {:name 1 :_id 0})))
