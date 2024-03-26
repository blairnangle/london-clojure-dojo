(ns _20240326.exercism)

(defn hello-world []
  "Hello, World!")

(def expected-time 40)

(defn remaining-time [actual_time]
  (- expected-time actual_time))

(defn add [x y]
  (+ x y))

(defn prep-time [number]
  (* number 2))

(defn total_time [layers time-elapsed]
  (+ (prep-time layers) time-elapsed))

(comment
  (hello-world)
  expected-time
  (remaining-time 10)
  (add 2 3)
  (prep-time 3)
  (total_time 3 20)
  )
