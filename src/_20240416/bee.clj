(ns _20240416.bee
  (:require [clojure.string :as string]
            [clojure.data.priority-map :as pm]))

(def n-words (atom 0))

(def scrabble-word-file "resources/scrabble.txt")

(defn dict->seq
  "Take a newline-delimited string of words and turn it into a sequence."
  [d]
  (map string/lower-case (string/split (slurp d) #"\n")))

(def scrabble-words-seq (dict->seq scrabble-word-file))

(defn has-7-unique-letters?
  "A bee needs to have seven unique letters."
  [word]
  (= (count (set word)) 7))

(defn has-at-least-4-letters?
  "Valid (sub) words that we can create from a bee must have at least four letters."
  [word]
  (>= (count word) 4))

(defn no-letter-s?
  "The letter s is never present in bees."
  [word]
  (not (.contains word "s")))

(def all-viable-bee-words
  (->>
    scrabble-words-seq
    (filter no-letter-s?)
    (filter has-7-unique-letters?)))

(def all-sub-words
  (->>
    scrabble-words-seq
    (filter no-letter-s?)
    (filter has-at-least-4-letters?)))

(defn get-central-letter-permutations
  "For a particular word, generate a sequence of tuples with all the central letter permutations.

  Each tuple in the returned seq: [word central-letter word-without-central-letter].

  The length of the returned seq should be equal to the number of unique letters in word."
  [word]
  (let [distinct-word-seq (distinct (seq word))]
    (map (fn [c] [word c (string/join (disj (set word) c))]) distinct-word-seq)))

(def all-central-letter-permutations
  "A flat sequence of all the word-central letter-word without central letter combinations."
  (apply concat
         (into []
               (map (fn [w] (get-central-letter-permutations w)))
               all-viable-bee-words)))

(defn get-words-for-bee
  "Get all the (sub) words for a bee.

  Take the bee minus the central letter, and the central letter separately as arguments for convenience."
  [bee-minus-c c]
  (filter (fn [w] (and (.contains w (str c))
                       (re-matches (re-pattern (str "[" (str bee-minus-c c) "]*")) w)))
          all-sub-words))

(defn score-word
  "As per the rules, four-letter words score one point, longer words score one point per letter."
  [word]
  (let [length (count word)]
    (if (= length 4)
      1
      length)))

(defn score-words [words]
  (reduce + (map score-word words)))

(defn bee->words&score []
  (into {} (map
             (fn [[w c bee-minus-c]]
               (let [bee-words (get-words-for-bee bee-minus-c c)]
                 (when (zero? (mod @n-words 100))
                   (println @n-words "processed! Word" @n-words "is:" w))
                 (swap! n-words inc)
                 {[w c bee-minus-c] {:words bee-words
                                     :score (score-words bee-words)}}))
             all-central-letter-permutations)))

(defn sort-by-score
  "Functions in the clojure.data.priority-map lib take a variable number of k-v pairs as arguments.
  Hence, we need to massage our input map somewhat.

  We use a priority map here so that we can sort by (nested) value."
  [bee-map]
  (println "Sorting word map. Nearly done!")
  (apply pm/priority-map-by
         (fn [l r] (< (:score l) (:score r)))
         (apply concat bee-map)))

(defn -main [& _args]
  (time
    (doall
      (println "Processing words…\n")
      (let [lowest-score (first (sort-by-score (bee->words&score)))
            [word central-letter _] (first lowest-score)
            {:keys [words score]} (second lowest-score)]

        (println (str "The word-central letter combination with the lowest score is: " word ", " central-letter " (" score ").\n"))
        (println (str "The valid sub-words for this combination are: " (string/join ", " words) ".\n"))))))

(comment

  (get-central-letter-permutations "aardwolf")
  (get-central-letter-permutations "equivoke")

  (get-words-for-bee "adflor" \w)
  (score-words
    (get-words-for-bee "adflor" \w))

  (time
    (doall
      (bee->words&score)))

  (first (apply pm/priority-map-by (fn [l r] (< (:score l) (:score r))) (apply concat (bee->words&score))))

  (apply pm/priority-map-by (fn [l r] (< (:score l) (:score r))) (apply concat {:a {:score 1}
                                                                                :b {:score 3}
                                                                                :c {:score 0}}))

  )
