(require '[babashka.fs :as fs]
         '[clojure.tools.cli :refer [parse-opts]])

(println (fs/directory? "."))

(def cli-options

  [["-f" "--file FILE" "File to read"]
   ["-s" "--string STR" "String to find to read"]])

(def options (:options (parse-opts *command-line-args* cli-options)))

(defn filter-grab-line-number [found]
  (filter #(when (some? %) (.indexOf found)) found))

(defn grep-file [file string]
  (let [lines (fs/read-all-lines file)
        found (map #(re-find (re-pattern string) %) lines)]
    (loop [l lines f found r []]
      (recur (rest l) (rest f) (if (some? f)
                                 (conj r {:line l})
                                 r)))))



(prn (map #(grep-file % (:string options)) (fs/glob "." (:file options))))
