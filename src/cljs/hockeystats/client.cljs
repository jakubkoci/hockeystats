(ns hockeystats.core
  (:require [goog.net.XhrIo :as xhr]
            [cljs.reader :as reader]))

;; Helpers
(defn sort-by-points [table]
  (into (sorted-map-by (fn [key1 key2]
                           (compare [(get table key2) key2]
                                    [(get table key1) key1])))
          table))


;; Define data and HTML components
(def standings (atom []))
(def slider (.getElementById js/document "slider"))
(def table (.getElementById js/document "teams-table-body"))


;; Initialize standings data and do initial table render
(defn init []
  (xhr/send "/standings" load-standings "GET"))

(defn load-standings [event]
  (let [response-text (.getResponseText (.-target event))
        response-data-structure (reader/read-string response-text)]
    (swap! standings concat-vectors response-data-structure)
    (render-table "1")))

(defn concat-vectors [vector-a vector-b]
  (into [] (concat vector-a vector-b)))


;; Handle table re-render when change round by slider
(defn handle-slider [event]
  (render-table event.target.value))

(defn render-table [round]
  (set! (. table -innerHTML) "")
  (doseq [team-key-value-pair (sort-by-points (nth @standings (js/parseInt round)))]
    (let [team (vec team-key-value-pair)
          table-row (.createElement js/document "tr")
          table-cell-place (.createElement js/document "td")
          table-cell-team (.createElement js/document "td")
          table-cell-points (.createElement js/document "td")]
      (.appendChild table-cell-place (.createTextNode js/document ""))
      (.appendChild table-cell-team (.createTextNode js/document (nth team 0)))
      (.appendChild table-cell-points (.createTextNode js/document (nth team 1)))
      (.appendChild table-row table-cell-place)
      (.appendChild table-row table-cell-team)
      (.appendChild table-row table-cell-points)
      (.appendChild table table-row))))


(.addEventListener slider "mousemove" handle-slider)

(init)
