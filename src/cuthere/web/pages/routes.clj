(ns cuthere.web.pages.routes
  (:require [ganelon.web.dyna-routes :as dyna-routes]
            [cuthere.web.pages.common :as common]
            [cuthere.web.middleware :refer [wrap-friend]]
            [compojure.core :refer [ANY]]))


(dyna-routes/defpage-ns :public "/" req
  (common/home-layout req))

(dyna-routes/defpage-ns :public "/login" req
  (common/login-layout req))

(dyna-routes/setroute! :auth
  (ANY "/*" []
    (-> (dyna-routes/route-ns-fn :public)
        wrap-friend)))
