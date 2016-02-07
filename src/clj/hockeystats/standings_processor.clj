(ns hockeystats.standings-processor
  (:require [hockeystats.csv-data-processor :as csv]))


(defn goals-home-team [periods]
  (reduce
    (fn [sum period] (+ (Integer/parseInt (subs period 0 1)) sum))
    0 periods))

(defn goals-visitor-team [periods]
  (reduce
    (fn [sum period] (+ (Integer/parseInt (subs period 2 3)) sum))
    0 periods))

(goals-home-team '("3:1" "1:0" "1:0"))
(goals-visitor-team '("3:1" "1:0" "1:0"))

(defn get-winner-points [periods]
  (if (> (count periods) 3) 2 3))

(defn game-to-points [game]
  (let [periods (:periods game)
        winner-points (get-winner-points (:periods game))]
    (if (> (goals-home-team periods) (goals-visitor-team periods))
      {(:team1 game) winner-points
       (:team2 game) (- 3 winner-points)}
      {(:team1 game) (- 3 winner-points)
       (:team2 game) winner-points})))

(defn game-to-points-helper [game-with-round]
  (game-to-points (:game game-with-round)))


(defn filter-by-round [round]
  (fn [game]
    (= (:round game) round)))

(defn table-for-round [games round]
  (->> (filter (filter-by-round (str round)) games)
       (map game-to-points-helper)
       (into {})
       (conj)))

(defn tables-for-rounds [games]
  (loop [n 2
         result []]
    (if (>= (count result) (count games))
      result
      (let [previous-table (last result)
            current-table (table-for-round games n)]
        (recur (inc n) (conj result (merge-with + previous-table current-table)))))))


(defn table-after-round [games round]
  (loop [n 1
         result (table-for-round games (str (- n 1)))]
    (if (>= n round)
      result
      (recur (inc n) (merge-with + result (table-for-round games n))))))


(defn sort-by-points [table]
  (into (sorted-map-by (fn [key1 key2]
                           (compare [(get table key2) key2]
                                    [(get table key1) key1])))
          table))

(defn sorted-tables-for-rounds [games]
  (map sort-by-points (tables-for-rounds games)))

(def games (csv/convert-games-from-csv "resources/games.csv"))
(sorted-tables-for-rounds games)

