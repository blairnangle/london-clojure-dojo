(ns _20240227.kangaroo
  (:require
    [clojure.test :refer [deftest testing is]]))

(defn- calc-crossing-point [left right]
  (/
    (- (:pos left) (:pos right))
    (- (:jump right) (:jump left))))

(defn kangaroo [x1 v1 x2 v2]
  (let [k1 {:pos  x1
            :jump v1}
        k2 {:pos  x2
            :jump v2}
        sorted-by-pos (sort-by :pos [k1 k2])
        left (first sorted-by-pos)
        right (last sorted-by-pos)]
    (cond
      (<= (:jump left) (:jump right)) "NO"
      (pos-int? (calc-crossing-point left right)) "YES"
      :else "NO")))

(deftest kangaroo-test
  (testing "yes or no"
    (is (= (kangaroo 0 3 4 2) "YES"))
    (is (= (kangaroo 0 2 5 3) "NO"))
    (is (= (kangaroo 43 2 70 2) "NO"))))

(comment
  (calc-crossing-point {:pos 0 :jump 3} {:pos 4 :jump 2})
  (calc-crossing-point {:pos 0 :jump 2} {:pos 5 :jump 3})
  (calc-crossing-point {:pos 43 :jump 2} {:pos 70 :jump 2})
  )
