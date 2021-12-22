;; Java & Clojure

;; A Java function is Clojure function
(.toUpperCase "eris boreas greyrat")

;; We can import java packages
(ns advanced
  (:import (java.io File InputStream)))


;; Threading

(defn do-something []
  (println "Hello from the thread.")
  (Thread/sleep 3000)
  (println "Goodbye from the thread.")
  (= 1 1))

(do-something)
(.start (Thread. do-something))
(.join (Thread. do-something))

;; Promises
;; values that are not necessarily known when created. 
;; But returns a result either successful or failed at some point in the future.
(def result (promise))

;; deliver returns a value to the promise
;; this can be a successful result or exception
(deliver result "Nami")

;; deref / @
;; As usual we need to unwrap our promise kinda like await
(print (str (result) "-chwannnnn")) ;; Notice this won't work because we cannot print promises
(print (str @result "-chwannnnn")) ;; Using @ to deref 
(print (str (deref result) "-chwannnnn"))

;; future
;; Clojure's promises, kinda same as Dart
;; Use future as much as possible, as they handles thread creation & disposing
;; Threads are normally expensive as they take a long time to created and requires more memory
(def revenue-future
  (future (apply + (map :revenue '({:revenue 1000} {:revenue 2000})))))

(print "Total Renvenue is:" @revenue-future)

;; pmap
;; kinda like Promise.all in JS


;; State
;; Remember Clojure is all about immutability
;; So it is not necssarily the same like how you would simply update the variable like in other languages

;; atom
;; essentially allows you to change the state of a variable
(def counter (atom 0))

;; swap!
;; changes state of atom
(defn add-counter []
  (swap! counter inc)
  (swap! counter + 12))

(add-counter)

;; accessing values using deref / @
@counter

;; atoms with maps
;; We can easily find this useful when tracking app configurations
(def drama
  (atom {:title "Jirisan" :genre "Mystery"}))

@drama
(swap! drama #(assoc % :genre "Hiking"))
(swap! drama #(dissoc % :genre))

;; refs
;; Makes tracking state of related atoms easy
;; Allows transactional updates

(def by-title (ref {}))
(def total-copies (ref 0))

@by-title
@total-copies

(defn add-book [{title :title :as book}]
  (dosync ;; basically to make it synchronous, "alter" must be used in dosync parens
   (alter by-title #(assoc % title book)) ;; alter provides a transactional-like syntax
   (alter total-copies + (:copies book))))

;; agents
;; like atoms are stand-alne value containers
;; updates using "send" instead of "swap!"s
;; main difference being "send" is asynchronous, "swap!" is synchronous

(def counter (agent 0))

;; some async function

@counter

;; read
;; Basically to read data
;; Use it only to read data that you trust

;; eval
;; turn characters into data structures
(def a-data-structure '(+ 2 2))

(def some-print-statement
  '(defn print-greeting [preferred-customer]
     (if preferred-customer (println "Hello!"))))

(eval a-data-structure)
(println a-data-structure)
(println "hello")
(println (eval a-data-structure))
;; (print-greeting true)

;; metadata
;; extra data you can hang on Clojure symbols and collections

;; :favourite-books is not added to the actual map
(def books1 (with-meta ["Emma" "1984"] {:favourite-books true}))
(def books1 ^:favourite-books ["Emma" :1984]) ;; with syntactical sugar

;; Macros
;; A macro is simply a way to pass in actual readable code to be executed in another function

(defn arithmetic-if [n pos zero neg]
  (cond ;; Note: cond evaluates ALL conditions. Not only the first one.
    (pos? n) (pos) ;; Notice how we wrap this in brackets. Its to excute the arguments!
    (zero? n) (zero) ;; Note: that only use this if you want to execute a side-effect (e.g. function call)
    (neg? n) (neg))) ;; Otherwise a simple return of the value will suffice

(defn print-rating [rating]
  (arithmetic-if rating
    #(println "Good book!") ;; We use a reader macro "#". Basically tell Clojure to take this statement but dont eval it.
    #(println "Totally indifferent.")
    #(println "Run away!")))

(print-rating 10)

;; Syntax quoting

;; The backticks "`" and quotes "'" prevents expression from being evaluated
`(:a :syntax "quoted" :list 1 2 3 4)
'(:a :syntax "quoted" :list 1 2 3 4)

(def n 100)
(def pos "Its a positive!")
(def neg "Its a negative!")
(def zero "Its a zero!")

`(cond
   (post? ~n) ~pos
   (zero? ~n ~zero)
   :else ~neg)

(defmacro arithmetic-if-macro [n pos zero neg]
  `(cond
     (pos? ~n) ~pos
     (zero? ~n) ~zero
     :else ~neg))
