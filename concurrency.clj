(ns concurrency
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alt! alts! alts!! timeout go-loop]]))

;; Future
;; Long running tasks can be wrapped into a future

(defn long-process [name ms]
  (println "Starting long process: " name)
  (println "Sleeping for " ms "ms")
  (Thread/sleep ms)
  (println "Done sleeping"))

(defn infinite-process [name]
  (println "Starting infinite process: " name)
  (loop []
    (println "To infinity and beyond")
    (Thread/sleep 10000)
    (recur)))

(def some-long-process-a 
  (future (long-process "long-process-A" 10000)))

(def some-infinite-process
  (future (infinite-process "Infinity")))

;; In the case we want to stop the future prematurely we can use future-cancel
;; Using future-cancel to stop thread
(future-cancel some-long-process-a)
(future-cancel some-infinite-process)

;; Using promises to handle start / stop
(defn play-music! []
  (let [p (promise)]
    (future
      (while (= (@p 0 ::timeout) ::timeout) ;; "::" is to ensure this var is in this namespace
        (println "I am playing music")))
    #(deliver p ::stop))) ;; Recall we are using a macro reader here

(let [stop-playing (play-music!)]
  (println "Imagine you are on the train")
  (println "Reached destination")
  (stop-playing))

;; core.async way

;; chan - Channels, used to communicate messages
;; messages can be put (>!!) into channel or take (<!!) from channel

;; go - creates new process. Runs concurrently on a separate thread.
;; 1) when trying to put a message on a channel or take a message off of it, wait and do nothing until the put or take succeeds
;; 2) when the put or take succeeds, continue executing.

(def simple-channel (chan)) ;; Declares a channel called simple-channel
(go (println (<! simple-channel))) ;; When message is taken from simple channel, print it
(>!! simple-channel "Welcome to my simple channel") ;; Put message into simple-channel

;; buffers
;; Restricts buffer size of channel

(def simple-buffer-channel (chan 2)) ;; Declare with buffer of 2

(defn set-interval
  [f time-in-ms]
  (let [stop (chan)] ;; Declares a channel called stop
    (go-loop []
      (alt!
        (timeout time-in-ms) (do (<!! (thread (f)))
                                 (recur))
        stop (println "Stopped synchronizing")))
    stop))

(defn infinity-and-beyond []
  (println "To infinity and beyond"))

(def job (set-interval #(println "Howdy") 2000))
(close! job)

;; thread
;; acts like a future, use when your process takes a long time before putting or taking
;; ^ creates new thread and execute process

(thread (println (<!! simple-channel)))
(>!! simple-channel "Hello my simple channel")

(go-loop [seconds [1 2 3]]
 (println "Hello world: " seconds)
  (timeout 1000))

(def simple-go (go
                 (while (not (<!! simple-channel))
                   (println "Inside channel")
                   (timeout 5000))
                 (println "stop go")))

(def simple-thread (thread
                     (while (not (<!! simple-channel))
                       (println "Inside thread")
                       (timeout 5000))
                     (println "stop thread")))

(>!! simple-channel true)

(>!! simple-channel false)


(defn start []
  (let [ctl (chan 0)]
    (go-loop []
      (alt!
        (timeout 2000) (do
                         (prn "tick")
                         (recur))
        ctl (prn "Exit ")))
    ctl))


(let [w (start)]
  (Thread/sleep 7000)
  (>!! w "stop"))

(def c (chan 1))
(go-loop []
  (let [x (<! c)]
    (println "Got a value in this loop:" x))
  (recur))

(>!! c "hello")
(close! c)