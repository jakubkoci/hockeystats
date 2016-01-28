(ns hockeystats.core)

;; (.write js/document "<h1>Bla bla bla lbalbalkjsdfk</h1>")


;; (defn writeDiv [team margin-top]
;;   (.write js/document
;;           (str "<div id=\"" team "\" style=\"margin-top: " margin-top "px\">" team "</div>")))

;; (writeDiv "Brno" 10)
;; (writeDiv "Zlin" 20)


(def teams-table-div (.getElementById js/document "teams-table"))
(def new-div (.createElement js/document "div"))
(def new-text (.createTextNode js/document "Praha"))
(.appendChild new-div new-text)
(.appendChild teams-table-div new-div)


(defn create-div [text margin-top]
  (let [div (.createElement js/document "div")
        textNode (.createTextNode js/document text)]
    (.appendChild div textNode)
    (.setAttribute div "id" text)
    (.setAttribute div "style" (str "margin-top: " margin-top "px"))
    (.appendChild teams-table-div div)))

(.log js/console (create-div "Pardubice" 200))

(.setAttribute (.getElementById js/document "Pardubice") "style" (str "margin-top: " 20 "px"))

(def slider (.getElementById js/document "slider"))

(defn handleSlider [event]
  (.log js/console event.target.value)
  (let [element (.getElementById js/document "Pardubice")]
    (.setAttribute element "style" (str "margin-top: " event.target.value "px"))))

(.addEventListener slider "mousemove" handleSlider)

