(ns hockeystats.server
  (:require [compojure.route :as route]
            [compojure.core :as compojure]
            [ring.util.response :as response]
            [ring.adapter.jetty :as jetty]))

(compojure/defroutes app
  (compojure/GET "/" request
    (response/resource-response "public/index.html"))
  (route/resources "/"))

(defn -main []
  (prn "View the example at http://localhost:4000/")
  (jetty/run-jetty app {:join? true :port 4000}))
