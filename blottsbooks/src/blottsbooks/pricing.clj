(ns blottsbook.pricing) ;; Creating another namespace for our project

(def discount-rate 0.15)
(defn discount-price [book]
  (- (:price book)
     (* discount-rate (:price book))))