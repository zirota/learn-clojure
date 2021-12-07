;; ns refers to namespace
(ns blottsbooks.core
  (:gen-class))

;; You need to define your functions before you use them
(defn say-welcome [what]
  (println what))

;; The main function (-main)
;; Run using lein run
(defn -main []
  (println say-welcome "Hazzen's House"))

;; The last defined wins
(def author "Hazzen")
(defn author [name]
  (println "Hello" name "is writing a book!"))
