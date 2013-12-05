(ns cuthere.web.pages.routes
  (:require [ganelon.web.dyna-routes :as dyna-routes]
            [cuthere.web.pages.common :as common]
            [cuthere.web.middleware :refer [wrap-friend]]
            [cuthere.utils :refer [dbg]]
            [cemerick.friend :as friend]
            [ring.util.response :as resp]
            [cuthere.web.handlers.registration :refer [confirm-user]]
            [compojure.core :refer [ANY]]))


(dyna-routes/defpage-ns :private "/" req
  (common/home-layout req))

(dyna-routes/defpage-ns :private "/login" req
  (common/login-layout req))

(dyna-routes/defpage-ns :private "/register" req
  (common/register-layout req))

(dyna-routes/defpage-ns :private "/confirmation/:confirmation-text"
  [confirmation-text & rest]
  (let [confirmed? (confirm-user (dbg confirmation-text))]
    (common/confirmation-layout confirmed?)))

(dyna-routes/defpage "/logout" req
  (friend/logout* (resp/redirect (str (:context req) "/"))))

(dyna-routes/setroute! :auth
  (ANY "/*" []
    (-> (dyna-routes/route-ns-fn :private)
        wrap-friend)))
