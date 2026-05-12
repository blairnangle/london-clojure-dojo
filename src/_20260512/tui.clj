#!/usr/bin/env bb

(babashka.deps/add-deps
  '{:deps {io.github.TimoKramer/charm.clj {:git/sha "cf7a6c2fcfcccc44fcf04996e264183aa49a70d6"}}})


(require '[charm.core :as charm]
         '[charm.components.progress :as progress]
         '[babashka.curl :as curl]
         '[charm.core :as charm]
         '[charm.components.progress :as progress]
         '[charm.message :as msg]
         '[charm.program :as program]
         '[babashka.curl :as curl])


(def my-bar (progress/progress-bar :width 40))

;; Update progress (0.0 to 1.0)
(def updated-bar (progress/set-progress my-bar 0.5))

;; In view function
(progress/progress-view updated-bar)

(def title-style
  (charm/style :fg charm/magenta :bold true))

(def count-style
  (charm/style :fg charm/cyan
               :padding [0 1]
               :border charm/rounded-border))

(defn update-fn [state msg]
  (cond
    (or (charm/key-match? msg "q")
        (charm/key-match? msg "ctrl+c"))
    [state charm/quit-cmd]

    (or (charm/key-match? msg "k")
        (charm/key-match? msg :up))
    [(update state :count inc) nil]

    (or (charm/key-match? msg "j")
        (charm/key-match? msg :down))
    [(update state :count dec) nil]

    ;(charm/key-match? msg "d")
    ;[(update state :stories (:body (http/get "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty"))) nil]

    :else
    [state nil]))

(defn tick-cmd []
  {:type :cmd
   :fn   (fn []
           (Thread/sleep 100)
           {:type :tick})})

(defn init []
  (let [stories (:body (curl/get "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty"))]
    [{:status     :start
      :stories    stories
      :downloaded []
      :msg        (str "There are " (count stories) " top stories - do you want to download them all? (y/n)\n")
      :bar        (progress/progress-bar :width 50
                                         :bar-style :default
                                         :show-percent true)}
     nil]))

(defn download-story [id]
  (:body (curl/get (str "https://hacker-news.firebaseio.com/v0/item/" id ".json?print=pretty"))))

(defn update-fn [state msg]
  (cond
    ;(msg/key-match? msg "q")
    ;[state program/quit-cmd]
    ;
    ;;(c)
    ;
    ;(msg/key-match? msg " ")
    ;[(assoc state :running (not (:running state)))
    ; (when-not (:running state) (tick-cmd))]
    ;
    ;(= :tick (:type msg))
    ;(let [bar (progress/increment (:bar state) 0.02)]
    ;  (if (progress/complete? bar)
    ;    [(assoc state :bar bar :running false) nil]
    ;    [(assoc state :bar bar)
    ;     (when (:running state) (tick-cmd))]))

    ;; the bar is always the previous bar incremented by 0.02 (2%)
    ;; if it's at 100%
    ;; then set the state to complete
    ;; else update state with the current value of bar, and if still running, ticket up again (i.e., sleep for a 100ms)

    (msg/key-match? msg "y")
    [(assoc state :downloaded (map download-story (:stories state)))
     "Downloading…"]

    (msg/key-match? msg "n")
    [[state program/quit-cmd]]

    :else
    [state nil]))



(defn view [state]
  (cond
    (= (:status state) :start) (str "There are " (count (:stories state)) " top stories. Do you want to download them all? (y/n)")
    (= (:status state) :running) (str "There are " (count (:stories state)) " top stories. Do you want to download them all?")
    (= (:status state) :done) "Oh, OK. All done."

    )

  #_(str "Download Progress\n\n"
         (progress/progress-view (:bar state)) "\n\n"
         (if (progress/complete? (:bar state))
           "Complete!"
           (if (:running state)
             "Downloading... (Space to pause)"
             "Press Space to start, Q to quit"))
         (:stories state)
         "\njust a string\n"
         ;(:body (curl/get "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty"))

         )

  )

(program/run {:init init :update update-fn :view view})
