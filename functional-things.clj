;; Using functions as basic building blocks


;; Creating functions on the fly
(fn [n] (* 2 n)) ;; we omit the name here
(println "multiply-by-2: " (fn [n] (* 2 n)))

;; Assing function to variable
(def double-it (fn [n] (* 2 n)))
(double-it 10)

(defn is-expensive [max-price]
  (fn [food]
    (when (>= (:price food max-price)
              food))))
(is-expensive 500)

;; apply
;; apply function to subsequent values
(def name ["Hazzen" "Chua"])
(apply list name)

;; partial

;; complement
(defn adventure? [book]
  (when (= (:genre book) :adventure)
    book))
(def not-adventure? (complement adventure?))
(def adventure-book 
  {:genre :adventure})

(not-adventure? adventure-book)

;; function literals (short form function without fn)
#(when (= (:genre %1) :adventure) % 1)