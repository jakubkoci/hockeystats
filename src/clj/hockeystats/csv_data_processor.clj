(ns hockeystats.csv-data-processor
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]))


(.exists (io/file "resources/games.csv"))

(def dirty-games
  (with-open [in-file (io/reader "resources/games.csv")]
    (doall
      (csv/read-csv in-file :separator \;))))

dirty-games


(defn get-round [array]
  (if (.contains (nth array 1) "kolo")
    (nth (str/split (nth array 1) #"[\\.\s]+") 0)
    nil))

(get-round ["" "1. kolo\n" "" "" "" "" "" ""])
(get-round ["" "11. kolo\n" "" "" "" "" "" ""])


(defn extract-periods [periods-string]
  (re-seq #".{1}:.{1}" periods-string))

(defn convert-to-game [game-vector]
  {:team1 (nth game-vector 2)
   :team2 (nth game-vector 6)
   :periods (extract-periods (nth game-vector 4))})

(convert-to-game ["" "" "HC Škoda Plzeň" "5" "PÁ 11. 09.\n(3:1, 1:0, 1:0)" "1" "\nHC Energie Karlovy Vary" ""])


(defn convert-games [games]
  (loop [games-left games
         round 0
         result []]

    (if (not= (next games-left) nil)
      (if (= (get-round (first games-left)) nil)
        (recur (rest games-left) round (conj result {:round round
                                                     :game (convert-to-game (first games-left))}))
        (recur (rest games-left) (get-round (first games-left)) result))
      result)))

(convert-games dirty-games)


;; (filter
;;   (fn [game]
;;     (= (:round game) 1))
;;   (convert-games trial-games))



