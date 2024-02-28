(ns kangaroo
  (:require
    [clojure.test :refer [deftest testing is]]))

(defn looper
  "For some n up to a (high) limit, if (n * v1 + x1 = n * v2 + x2) => YES else NO.

  Brute force, essentially. This *should* time out on HackerRank tests... but doesn't!"
  [x1 v1 x2 v2 n]
  (if (= (+ x1 (* n v1))
         (+ x2 (* n v2)))
    "YES"
    (if (= n 10000)
      "NO"
      (looper x1 v1 x2 v2 (inc n)))))

(defn kangaroo [x1 v1 x2 v2]
  (cond

    ;; return early in trivial cases
    (and (< x1 x2) (< v1 v2)) "NO"
    (and (<= x1 x2) (< v1 v2)) "NO"
    (and (< x1 x2) (<= v1 v2)) "NO"
    :else (looper x1 v1 x2 v2 1)))

(deftest kangaroo-test
  (testing "yes or no"
    (is (= (kangaroo 0 3 4 2) "YES"))
    (is (= (kangaroo 0 2 5 3) "NO"))))
