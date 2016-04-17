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


;; TODO this remove all of S, but I want to remove only the first one
(defn remove-line-ends [string]
  (str/replace string #"\n|S\s+\n" ""))


(defn extract-periods [periods-string]
  (re-seq #".{1}:.{1}" periods-string))


(defn convert-game [dirty-game]
  {:team1 (remove-line-ends (nth dirty-game 1))
   :team2 (remove-line-ends (nth dirty-game 5))
   :periods (extract-periods (remove-line-ends (nth dirty-game 3)))})

(convert-game ["" "" "BK \nMladá Boleslav" "1" "PÁ 18. 09.\n(0:0, 0:0, 1:1 - 0:0 - 0:1)" "2" "SHC Sparte Olomouc" ""])

(defn get-round [dirty-game]
  (if (.contains (nth dirty-game 0) "kolo")
    (nth (str/split (nth dirty-game 0) #"[\\.\s]+") 0)
    nil))


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


;; Usage example
;; (convert-games-from-csv "resources/games.csv")





