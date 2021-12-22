(ns blottsbooks.core-test ;; Convention: <module>-test
  (:require [clojure.test :refer :all]
            [blottsbooks.core :as core])) ;; we need :require the modules involved

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(def books
  [{:title "A Promised Land" :author "Obama" :copies 100}
   {:title "The Unicorn Project" :author "Gene Kim" :copies 50}
   {:title "A Random Walk Down Wall Street" :author "Butron Malkiel" :copies 1000}])

(deftest test-finding-books ;; Note the naming
  ;; Note the function we are testing find-by-title is clearly stated
  ;; "is" is just Clojure's assertion function 
  (is (not (nil? (core/find-by-title "A Promised Land" books)))))

;; Running singular test
(test-finding-books)

;; Running all tests in a namespace
;; Note: Remember to import test library as necessary
(clojure.test/run-tests)
(clojure.test/run-tests *ns*)
(clojure.test/run-tests 'blottsbooks.core-test)

;; lein test - runs all tests

;; Property-Based Testing

;; Generating data
;; Using test.check (Note that we need to require it in project.clj dependencies)
(require '[clojure.test.check :as tc])
(require '[clojure.test.check.generators :as gen])

(gen/sample gen/string-alphanumeric)

;; (def title-gen gen/string-alphanumeric)
;; (def author-gen gen/string-alphanumeric)
;; (def copies:gen gen/nat)

;; Using such-at to specify conditions

(def title-gen (gen/such-that not-empty gen/string-alphanumeric))
(def author-gen (gen/such-that not-empty gen/string-alphanumeric))
(def copies-gen (gen/such-that (complement zero?) gen/pos-int))

title-gen
author-gen
copies-gen

;; Generating hashmaps
(def book-gen
  (gen/hash-map :title title-gen :author author-gen :copies copies-gen))

(gen/sample book-gen)

;; Generating vectors
(def inventory-gen (gen/not-empty (gen/vector book-gen)))

;; Plucking out item using element
(def inventory-and-book-gen
  (gen/let [inventory inventory-gen
            book (gen/elements inventory)]
           {:inventory inventory :book book}))

(gen/sample inventory-and-book-gen)


(deftest test-divide 
  (is (= 1/2 (core/divide 1 2)))
  (is (= 1/2 (core/divide 3 6)))
  (is (= 1/2 (core/divide 1 2))))

(test-divide)
