(ns hockeystats.standings-processor
  (:require [hockeystats.csv-data-processor :as csv]))


(def games (csv/convert-games-from-csv "resources/games.csv"))

(defn filter-by-round [game]
  (= (:round game) "1"))

(def games-round1 (filter filter-by-round games))

games

games-round1

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

(game-to-points (:game (first games-round1)))

(def table-for-round (conj (into {} (map game-to-points-helper games-round1))))

(defn sort-by-points [table-for-round]
  (into (sorted-map-by (fn [key1 key2]
                           (compare [(get table-for-round key2) key2]
                                    [(get table-for-round key1) key1])))
          table-for-round))

(sort-by-points table-for-round)

(def teammap
  {"HC Škoda Plzeň" 3
   "HC Energie Karlovy Vary" 0})

(get teammap "HC Škoda Plzeň")
(update-in teammap ["HC Škoda Plzeň"] + 3)
