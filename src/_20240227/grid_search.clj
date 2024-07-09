(ns _20240227.grid-search
  (:require
    [clojure.test :refer [deftest testing is]]))

(defn matching-indices [str-pattern s]
  (loop [m (re-matcher (re-pattern str-pattern) s)
         res #{}]
    (if (.find m)
      (recur m (conj res (.start m)))
      ;; HackerRank uses Clojure 1.9 and update-keys was only added in 1.11
      ;(update-keys (frequencies res) (comp keyword str)))))

      (into {} (map (fn [[i c]] {(keyword (str i)) c}) (frequencies res))))))

(defn update-history [history row pattern-rows]
  (into {} (filter val (merge
                         (reduce-kv (fn [m idx count]
                                      (let [matches (matching-indices (nth pattern-rows count) row)]
                                        (assoc m idx (if (contains? matches idx) (inc count) nil))))
                                    {}
                                    history)
                         (matching-indices (first pattern-rows) row)
                         ))))

(defn gridSearch [G P]
  (loop [grid-rows G
         history []]
    (cond
      (some #(= (count P) %) (map val history)) "YES"
      (empty? grid-rows) "NO"
      :else
      (do
        (println (update-history history (first grid-rows) P))

        (recur


          (rest grid-rows)
          (update-history history (first grid-rows) P))))))


(deftest tests
  ;(testing "should answer YES if the pattern is in the grid"
  ;  (let [grid-rows ["000000"
  ;                   "001230"
  ;                   "004560"
  ;                   "007890"
  ;                   "000000"]
  ;        pattern-rows ["123"
  ;                      "456"
  ;                      "789"]]
  ;
  ;    (is (= (gridSearch grid-rows pattern-rows) "YES")))
  ;
  ;  (testing "should answer NO if the pattern is not in the grid"
  ;    (let [grid-rows ["000000"
  ;                     "001230"
  ;                     "004560"
  ;                     "007890"
  ;                     "000000"]
  ;          pattern-rows ["99"
  ;                        "99"]]
  ;
  ;      (is (= (gridSearch grid-rows pattern-rows) "NO"))))
  ;
  ;  (testing "ensure that pattern rows are aligned"
  ;    (let [grid-rows ["000000"
  ;                     "110000"
  ;                     "110011"
  ;                     "000011"
  ;                     "000000"]
  ;          pattern-rows ["11"
  ;                        "11"
  ;                        "11"]]
  ;
  ;      (is (= (gridSearch grid-rows pattern-rows) "NO"))))

  (testing "partial pattern matches on previous grid rows should not result in false negatives"
    (let [grid-rows ["000000"
                     "110000"
                     "110011"
                     "000011"
                     "000011"]
          pattern-rows ["11"
                        "11"
                        "11"]]

      ;; this is failing because we actually have no way of knowing if 11 should count as the second pattern row
      ;; OR if it is just the first row shifted one down

      (is (= (gridSearch grid-rows pattern-rows) "YES")))))

#_(testing "HackerRank unit test #7"
    (let [grid-rows ["123412"
                     "561212"
                     "123634"
                     "781288"]
          pattern-rows ["12"
                        "34"]]

      (is (= (gridSearch grid-rows pattern-rows) "YES"))))
;)

(comment

  )
