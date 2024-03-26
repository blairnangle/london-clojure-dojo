(ns grid-search
  (:require
    [clojure.string :as string]
    [clojure.test :refer [deftest testing is]]))

(defn grid-search [G P]
  (let [grid-rows (string/split G #"\n")
        pattern-rows (string/split P #"\n")
        first-pattern-row (first pattern-rows)]

    (doseq [row grid-rows
            matched (repeat 3 false)
            ]
      (if (string/includes? row first-pattern-row)
        "YES"
        "NO"))))

(comment
  (grid-search "7283455864\n6731158619\n8988242643\n3830589324\n2229505813\n5633845374\n6473530293\n7053106601\n0834282956\n4607924137"
               "9505\n3845\n3530")
  )
