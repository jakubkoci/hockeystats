(ns hockeystats.standings-processor-test
  (:require [clojure.test :refer :all]
            [hockeystats.standings-processor :as standings]))


(deftest returns-goals-from-periods
  (is (= 5 (standings/goals-home-team '("3:1" "1:0" "1:0"))))
  (is (= 1 (standings/goals-visitor-team '("3:1" "1:0" "1:0")))))


(run-all-tests)
