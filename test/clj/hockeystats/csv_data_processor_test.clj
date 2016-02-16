(ns hockeystats.csv-data-processor-test
  (:require [clojure.test :refer :all]
            [hockeystats.csv-data-processor :as csv]))


(deftest converts-game-to-data-structure
  (is (=
        {:team1 "BK Mladá Boleslav"
         :team2 "HC Olomouc"
         :periods '("0:0" "0:0" "1:1" "0:0" "0:1")}
        (csv/convert-game ["" "" "BK Mladá Boleslav" "1" "PÁ 18. 09.\n(0:0, 0:0, 1:1 - 0:0 - 0:1)" "2" "S\nHC Olomouc" ""])
        )))


(deftest returns-round-from-game-vector
  (is (= "1" (csv/get-round ["" "1. kolo\n" "" "" "" "" "" ""])))
  (is (= "11" (csv/get-round ["" "11. kolo\n" "" "" "" "" "" ""]))))


(run-all-tests)

