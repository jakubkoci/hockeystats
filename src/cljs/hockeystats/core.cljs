(ns hockeystats.core)


(def teams-table-div (.getElementById js/document "teams-table"))

(def slider (.getElementById js/document "slider"))

(def teams {(keyword "1") ["a" "b" "c"]
            (keyword "2") ["b" "a" "c"]
            (keyword "3") ["b" "a" "c"]
            (keyword "4") ["b" "c" "a"]
            (keyword "5") ["c" "b" "a"]
            (keyword "6") ["c" "a" "b"]})

(.log js/console (nth ((keyword "1") teams) 1))

(defn handleSlider [event]
  (.log js/console event.target.value)
  (render-table event.target.value))

(.addEventListener slider "mousemove" handleSlider)


(def table (.getElementById js/document "teams-table-body"))

(defn render-table [round]
  (set! (. table -innerHTML) "")
  (doseq [[team] ((keyword round) teams)]
    (.log js/console team)
    (let [table-row (.createElement js/document "tr")]
    (.appendChild table-row (.createTextNode js/document team))
    (.appendChild table table-row))))

