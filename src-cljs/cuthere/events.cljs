(ns cuthere.eventss
  (:require [enfocus.core :as ef]
            [enfocus.events :as ef-events]
            [ajax.core :refer [GET POST]])
  (:require-macros [enfocus.macros :as em]))


(defn handler [response]
  (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))


(defn create-event []
   (let [values (ef/from "#create-event-form" (ef/read-form))]
    (POST "/create-event" {:params {:event values}
                           :handler handler
                           :error-handler error-handler
                           :response-format :json
                           :keywords? true}))
  "ok")

(em/defaction change [msg]
  ["#create-event-form"] (ef/content (create-event)))

(em/defaction setup []
  ["#create-event-button"] (ef-events/listen :click #(change "asd")))

(set! (.-onload js/window) setup)
