;; ns refers to namespace
(ns blottsbooks.core
  (:require blottsbook.pricing) ;; Folding require to ns. Note that it is a keyword here
  (:gen-class))

(require '[blottsbook.pricing :as pricing]) ;; Creating separate alias

;; Alternative creating separate alias
;; Alias created by :as is local!
;; (ns blottsbooks.core
;;   (:require blottsbook.pricing :as pricing)
;;   (:gen-class))

;; Using var from another namespace usign :refer
(require '[blottsbook.pricing :refer [discount-rate]])

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


;; Looking up ns vars
(println "Current ns:" *ns*)

;; Note the ' symbol
(find-ns 'blottsbook.pricing)

;; Get all vars defined
(ns-map (find-ns 'user))

;; Require has a load-once only 
;; To re-require either restart Repl or do :reload
(require :reload '[blottsbook.pricing :as pricing])

;; :reload only loads new vars to clear out existing vars from ns do :ns-unmap
(defonce some-value 100) ;; defonce is like a constant
(ns-unmap *ns* 'some-value)