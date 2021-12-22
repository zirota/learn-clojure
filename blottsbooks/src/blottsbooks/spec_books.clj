(ns blottsbooks.spec-books
  (:require [clojure.spec.alpha :as s]))

;; Spec
;; They are like types but with Clj we focus on the data shape

;; valid checks if spec is followed
;; I would like to think its like ajv in JS
(s/valid? number? 44)
(s/valid? number? :hello)

;; specing collections
(def coll-of-strings (s/coll-of string?))
(coll-of-strings '("Mushoku Tensei")) ;; This breaks somehow?

;; for tighter specs "cat"
(def s-n-s-n (s/cat :s1 string? :n1 number? :s2 string? :n2 number?))
(s/valid? s-n-s-n ["Rudeus" 22 "Greyrat" 1312])

;; specing with keywords

(s/def ::author string?)
(s/def ::title string?)
(s/def ::copies pos-int?)
(s/def ::book
  (s/keys :req-un [::title ::author ::copies]))


;; specing a function
;; So, ideally fdef will find the function and assert using the spec created
(s/fdef find-by-title
  :args (s/cat ::title ::title))

(= (set '(:a :b :c )) #{:a :b :c})

