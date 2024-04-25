# 2024-03-12: Word ladder

## Problem

- We are given a start word and an end word and tasked with finding a route between them
- We can change one letter at a time
- But we can only change to a valid word at each step
- Output could be the list of words that we have moved through at each step

## Approach

- Build a graph of the different-by-one relationships between words from a dictionary
- The result should ultimately be a map of the form:
  ```clojure
  {"_ool" ["fool" "pool" …]
   "f_ol" ["fool"]
   "fo_l" ["fool" "fowl" …]
   "foo_" ["fool" "food" …]}
  ```
- The values of this map represent words that are all exactly one letter different from each other
- Build a tree in the proces of traversing the map
- Take the start word as the first node
- Calculate the next level of the tree (child nodes) by finding all the words that exactly one letter different
- Repeat this process recursively for each child node at each level of the tree, keeping a track of the path traversed
  thus far for each node
- When calculating child nodes for each node, exclude any words that have already been seen (if a word has already been
  seen we can safely say that there already exists a shorter path to get to this word)
- Always calculate a whole tree-level before moving on (i.e., we are performing a _breadth-first search_ of the word
  graph)
- Once we come across the end word, exit our recursive loop and return the path(s) of words that led us to the end word

## Result

```shell
time clojure -M -m _20240312.word-ladder "WORK" "TECH"
```

```shell
([WORK PORK PERK PECK PECH TECH])
clojure -M -m _20240312.word-ladder "WORK" "TECH"  2.44s user 0.18s system 241% cpu 1.089 total
```

## References

- [Bradfield School of Computer Science](https://bradfieldcs.com/algos/graphs/word-ladder/)
