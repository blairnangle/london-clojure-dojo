(ns _20240312.word-ladder
  (:require [clojure.string :as string]
            [clojure.set :as set]
            [clojure.test :refer [deftest testing is]]))

(defonce dict (string/split (slurp "./resources/scrabble.txt") #"\n"))

(defn get-masked-permutations-for-word
  "Iteratively replace each letter of `word` with \"_\".
  I.e., return a list with as many elements as there are letters in `word`.

  E.g., FOOL -> '(_OOL F_OL FO_L FOO_)"
  [word]
  (map-indexed (fn [idx char-v] (apply str (assoc char-v idx \_))) (repeat (count word) (vec word))))

(defn update-graph-with-bucket
  "Either create a new k-v entry in `graph` or append to the list of words if a mask already exists."
  [graph word]
  (loop [g graph
         masks (get-masked-permutations-for-word word)]
    (if (empty? masks)
      g
      (let [mask (first masks)]
        (recur
          (if (get g mask)
            (update g mask conj word)
            (assoc g mask (vector word)))
          (rest masks))))))

(defn build-graph
  "Build an undirected, unweighted graph to represent the relationships between words that are one letter apart.

  Create \"buckets\" of words where each bucket has one letter replaced with \"_\".
  This will act as the key in a map where the value is the list of words that fall into this bucket.
  Hence, all the words in the said list are different from each other by exactly one letter."
  [dict]
  (loop [graph {}
         word (first dict)
         words (rest dict)]
    (if (empty? words)
      graph
      (recur (update-graph-with-bucket graph word) (first words) (rest words)))))

(defn get-unseen-different-by-one
  "TODO…"
  [word graph seen]
  (mapcat
    (fn [p] (concat (filter #(and (not= word %) (not (contains? seen %))) (get graph p))))
    (get-masked-permutations-for-word word)))

(defn build-paths-for-level
  "From previously visited paths in the graph, calculate the new (never-before-seen) nodes.
  Append these new child nodes to the existing paths.

  Return value is a list of vectors."
  [graph seen paths]
  (loop [p paths
         new-paths []
         s seen]
    (if (empty? p)
      new-paths
      (recur
        (rest p)
        (concat new-paths (map #(conj (first p) %) (get-unseen-different-by-one (last (first p)) graph s)))
        (set/union (set (get-unseen-different-by-one (last (first p)) graph s)) s)))))

(defn bfs
  "Takes a graph, a starting node, `start` and target end node, `end`.

  Traverses the graph by building out a tree where each new level of the tree is computed from the parent node(s) and `graph`.

  Each node can only appear at a single level."
  [graph start end]
  (loop [paths [[start]]
         seen #{start}]
    (if (contains? seen end)
      (filter #(= (last %) end) paths)
      (recur
        (concat paths (build-paths-for-level graph seen paths))
        (set/union seen (set (flatten (build-paths-for-level graph seen paths))))))))

(defn -main [& _args]
  (println (bfs (build-graph dict) (first _args) (second _args))))

(deftest tests
  (let [graph (build-graph dict)]
    (testing "can get words that are different by one letter"
      (is (= (get-unseen-different-by-one "FOOL" graph #{})
             '("COOL"
                "MOOL"
                "POOL"
                "TOOL"
                "WOOL"
                "FOAL"
                "FOIL"
                "FOUL"
                "FOWL"
                "FOOD"
                "FOOT"))))

    (testing "can build one tree-level of graph traversal"
      (is (= (build-paths-for-level graph #{"FOOL"} [["FOOL"]])
             '(["FOOL" "COOL"]
               ["FOOL" "MOOL"]
               ["FOOL" "POOL"]
               ["FOOL" "TOOL"]
               ["FOOL" "WOOL"]
               ["FOOL" "FOAL"]
               ["FOOL" "FOIL"]
               ["FOOL" "FOUL"]
               ["FOOL" "FOWL"]
               ["FOOL" "FOOD"]
               ["FOOL" "FOOT"]))))))

(comment
  (bfs (build-graph dict) "FOOL" "SAGE")
  (bfs (build-graph dict) "WORK" "TECH")

  (build-paths-for-level (build-graph dict) #{"FOOL"} [["FOOL"]])

  (build-paths-for-level (build-graph dict) #{"FOOL"} [["FOOL" "COOL"]
                                                       ["FOOL" "MOOL"]])


  (get-unseen-different-by-one "FOOL"
                               (build-graph dict)
                               #{"FOOL"})

  (build-graph dict)

  (update-graph-with-bucket {} "fool")

  )
