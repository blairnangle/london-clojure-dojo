(ns _20250311.sumplete)

(def selection-ratio 0.5)

(defn n-random-elements [n coll]
  (take n (shuffle coll)))

(defn transpose [matrix]
  (apply mapv vector matrix))

(defn rand-1->9 []
  (rand-nth '(1 2 3 4 5 6 7 8 9)))

(defn mark-zeroes [row]
  (let [row-size (count row)
        n_elems (int (* row-size selection-ratio))
        indexes-to-zero (n-random-elements n_elems (range 1 row-size))]
    (map-indexed (fn [i e] (if (some #{i} indexes-to-zero) 0 e)) row)))

(defn puzzle-generator [n]
  (let [grid (repeatedly n #(take n (repeatedly (fn [] (rand-1->9)))))
        grid-with-zeroes (map mark-zeroes grid)
        row-sums (map #(reduce + %) grid-with-zeroes)
        column-sums (map #(reduce + %) (transpose grid-with-zeroes))]

    {:grid        grid
     :row-sums    row-sums
     :column-sums column-sums}))

(comment
  (puzzle-generator 9)
  )
