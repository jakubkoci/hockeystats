(ns hockeystats.server
  (:require [compojure.route :as route]
            [compojure.core :as compojure]
            [ring.util.response :as response]
            [ring.adapter.jetty :as jetty]
            [hockeystats.csv-data-processor :as csv]
            [hockeystats.standings-processor :as standings]))

(def games (csv/convert-games-from-csv "resources/games.csv"))
(def data (vec (standings/sorted-tables-for-rounds games)))

(compojure/defroutes app
  (compojure/GET "/" [request]
    (response/resource-response "public/index.html"))
  (compojure/GET "/standings" [request] (str data))
  (route/resources "/"))

(defn -main []
  (prn "View the example at http://localhost:4000/")
  (jetty/run-jetty app {:join? true :port 4000}))
