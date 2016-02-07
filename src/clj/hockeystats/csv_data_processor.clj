(ns hockeystats.csv-data-processor
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]))


(defn load-games [filename]
  (if (.exists (io/file filename))
    (with-open [in-file (io/reader filename)]
      (doall
        (csv/read-csv in-file :separator \;)))
    (throw (Exception. "File not found"))))


(defn remove-line-ends [string]
  (str/replace string "\n" ""))

(defn extract-periods [periods-string]
  (re-seq #".{1}:.{1}" periods-string))

(defn convert-game [dirty-game]
  {:team1 (remove-line-ends (nth dirty-game 2))
   :team2 (remove-line-ends (nth dirty-game 6))
   :periods (extract-periods (remove-line-ends (nth dirty-game 4)))})

(convert-game ["" "" "HC Škoda Plzeň" "5" "PÁ 11. 09.\n(3:1, 1:0, 1:0)" "1" "\nHC Energie Karlovy Vary" ""])

(defn get-round [dirty-game]
  (if (.contains (nth dirty-game 1) "kolo")
    (nth (str/split (nth dirty-game 1) #"[\\.\s]+") 0)
    nil))

(get-round ["" "1. kolo\n" "" "" "" "" "" ""])
(get-round ["" "11. kolo\n" "" "" "" "" "" ""])


(defn convert-games [games]
  (loop [games-left games
         round 0
         result []]

    (if (next games-left)
      (if (= (get-round (first games-left)) nil)
        (recur (rest games-left) round (conj result {:round round
                                                     :game (convert-game (first games-left))}))
        (recur (rest games-left) (get-round (first games-left)) result))
      result)))


(defn convert-games-from-csv [filename]
  (convert-games (load-games filename)))

(convert-games-from-csv "resources/games.csv")





