(require '[babashka.fs :as fs]
         '[clojure.tools.cli :refer [parse-opts]])

(def cli-options

  [["-f" "--file FILE" "File to read"]
   ["-s" "--string STR" "String to find to read"]
   ["-r" "--root ROOT" "Root directory for search" :default "."]])

(def options (:options (parse-opts *command-line-args* cli-options)))


(defn grep-file [file string]
  (let [lines (fs/read-all-lines file)
        found (map #(re-find (re-pattern string) %) lines)
        zmap (map vector (range 1 (inc (count lines))) lines found)
        filtered (filter #(not (nil? (last %))) zmap)]
    filtered))


(defn grep-files
  [file string root]
  (for [f (fs/glob root file)
        :let [found (grep-file f string)]
        :when (some? (last found))]
    {:file (str f) :matches found}))



(prn (grep-files (:file options) (:string options) (:root options)))



