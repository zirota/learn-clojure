;; This is a hello world comment

;; Ctrl + Enter to evaluate

;; Printing some strings
(println "Hello World!")
(print "Hello World!")
(println "Hello" "World")
(println true) ;; Booleans

;; Creating a string
(str "Hellow")
(str "Hello" "World")
(str "Hello" 1)
(str "I" "want" "to" "eat" 2 "icecream")

;; Counting characters in a string
(count "Hungry")

;; Combining functions
(count (str "I" "want" "to" "eat" 2 "icecream"))

;; Basic Math (Remember to apply function to entire parentheses left to right)
(+ 1 1 3.0) ;; Adding numbers
(- 69 69) ;; Substracting
(/ 144 12) ;; Division
(/ 144 12 2 3) ;; Division
(/ 9 2) ;; Fraction
(/ 9 2.0) ;; Division Floating Point
(/ 9.0 2) ;; Division Floating Point
(* 5 5) ;; Multiply
(/ (+ 1984 2008) 2) ;; Complex 1996

;; Variable Assignment (Notice that it is in kebab case)
(def first-name "Hazzen")
(def birth-year (/ (+ 1984 2008) 2))
(print birth-year)

;; Function
(defn hello-world []
  (println "Hello World")) ; No params
(hello-world)

(defn print-this [this-string]
  (print this-string)) ;; With params
(print-this "Hazzen Chua")

;; Clojure always returns the last statement
(defn chatty-average [a b]
  (println "Running chatty average")
  (println "1st value is:" a)
  (println "2nd value is:" b)
  (/ (+ a b) 2))
(chatty-average 10 20) ; returns 15

;; Creating a new app
;; lein new-app [app-name]


;; Exceptions
(/ 100 0)

;; Vectors
[1 2 3 4]
["can" 2 3 "mix" true]
["nested" ["vectors"]]

;; Creating a vector from a function
(vector "1" 2 "3" true)

;; Working with Vectors
(def some-books ["Harry" "Potter" "And" "The" "Deathly" "Hallows"])
(count some-books) ; count number of items
(first some-books) ; return first item
(rest some-books) ; return everything in a sequence except first
(rest (rest some-books))
(nth some-books 2) ; return item based on index
(some-books 2) ; return item based on index
(conj some-books "Part 1") ; returns a new vector adding item to end of vector
(cons some-books "Watch") ; returns a new vector adding item to start of vector
(print some-books)

;; Lists
'(1 2 3) ; Quote to precede square brackets to indicate as data 
'("can" 2 3 "mix" true)
'("nested" ("vectors"))
(list "create" "from" "function")

;; Lists vs Vectors
;; Fundamentally different
;; Lists are linked-lists, Vectors are arrays (continuous chunks of memory)
;; Lists faster to insert item (just add to head), Vector easier to retrieve item
(conj ["notice" "the" "difference"] "added to back")
(conj '("notice" "the" "difference") "added to front")

(def some-numbers [1 2 3])
(inc some-numbers)

;; Maps
{"title" "Harry Potter" "author" "JK Rowling" "published" 1996} ;; generic map
(hash-map "title" "Harry Potter" "author" "JK Rowling" "published" 1996)
(sorted-map "title" "Harry Potter" "author" "JK Rowling" "published" 1996)
(def book {"title" "Harry Potter" "author" "JK Rowling" "published" 1996})

;; Retrieve value
(get book "published")
(book "published")

;; Keywords *usually we will use keywords to represent keys in maps
:title
:author

(def book
  {:title "Harry Potter"
   :author "JK Rowling"
   :published 1996})

(println "Title: " (book :title))

;; Adding to maps by copying
(assoc book :subtitle "Half-blood Prince")

;; Removing from map by copying
(dissoc book :subtitle)

;; Get all keys
(keys book) ; returns as list
;; Get all values
(vals book)

;; Checking for key
(contains? book :title)
(contains? book "Harry Potter")

;; Maps are treated sequences of two-element vectors
;; Order is not guaranteed
(first book)
(second book)
(last book)
(count book)

;; SETS
(def genres #{:sci-fi :romance :mystery})
; Will throw DuplicateException if repeated values

(def adele-songs #{"Someone Like You" "Hello" "Easy On Me"})
(contains? adele-songs "Hello") ;; Checks if exists
(contains? adele-songs "The A Team")

(adele-songs "Hello")
(adele-songs "Its Me") ; nil if don't exist

;; Adding to Set
(conj adele-songs "Rolling In The Deep")
;; Removing
(disj adele-songs "Easy On Me")

;; Boolean Logic
(defn hello-reply [is-hello] ;; will automatically return nil
  (if is-hello
    (println "Hello from the other side!!")))
(def is-hello (contains? adele-songs "Hello"))
(hello-reply is-hello)
(if is-hello "I am truthy")
(if (not is-hello) "I am falsey so return nil")

;; Evaluating equality
(= 1 1)
(= 1 1 1 1 1 1 11) ;; false 
(= 2 (+ 1 1))
(= "Hazzen" "Chua")
(not= "Hazzen" "Chua")
(= "Hazzen" 1996)

(if (> 8 3)
  (println "8 bigger than 3"))

(if (>= 8 8)
  (println "8 more than or equals to 8"))

(if (and true true)
  (println "I am true"))

(if (or true false)
  (println "Something is false"))

;; Type checking
(number? 1996)
(number? "Ed Sheeran")
(string? "Seah")
(keyword? "song")
(keyword? :song)
(map? {})
(map? "Someone like you")
(vector? 1996)
(vector? [1996])

;; Non-nil values are truthy
;; *notice how we can craft if-else without parantheses
(if "Lego House"
  "I am gonna pick up the pieces"
  "Red")

(if false "Red" "All Too Well") ;; return "All Too Well"
(if [] (println "An empty vector is also true"))
(​if [] (println "An empty vector is also true")) ;; This is copy pasted, gives syntax error

;; Do
;; execute several expressions in sequence
(do
  (println "We could have had it all")
  (println "Rolling in the deep")
  (= 1 1)
  1996)

;; When
;; execute several expressions in sequence base on boolean
(when true
  (println "Go easy")
  (println "easy~~")
  (println "on me"))

;; cond
;; basically like switch-case but only truthy values
(defn ed-songs [song]
  (cond
    (= song "Give Me Love") true
    (= song "Hello") "not ed"
    :else false)) ;; catch-all statement
(ed-songs "Hello")

;; case
;; is like switch-case
(defn airplane-class [class]
  (case class
    :business "Here is your flat bed and menu"
    :economy "Chicken or Fish?"))
(airplane-class :economy)

;; Exception Handling
(try
  (/ 0 0)
  (catch ArithmeticException e (println "Math Problem:" e)))


;; Multi parameters function (kinda like method overloading)
(defn sing
  ([song] (println "Singing song:" song))
  ([song artist] (println "Singing song:" song "by artist:" artist)))

(sing "All Too Well")
(sing "All Too Well" "Taylor Swift")

;; Making use of overloaded function
(defn sing
  ([song] (println "Singing song:" song))
  ([song artist] (sing (str song "by artist:" artist))))

(sing "Lego House" "Ed Sheeran")

;; "Infinite" arguments
(defn beast-pirates
  ([& args] (println "Beast pirates:" args)))

(beast-pirates "Kaido" "King" "Queen" "Jack")

;; Using positional arguments with variadic arguments
(defn beast-pirates
  ([captain & args] (println "Captain:" captain "Beast pirates:" args)))

(beast-pirates "Kaido" "King" "Jack" "Queen")

;; Multi-methods
;; Multi-methods are functions that returns the implementation based on the parameters it gets
;; Methods need not be declared in the same file and can be added as and when necessary

;; Exmaple: Transforming book values from to use keywords
;; 1) Check for data format
;; *Good practice to return :default, else exception will be thrown
(defn get-book-format [book]
  (cond
    (vector? book) :vector-book
    (contains? book :title) :standard-map
    (contains? book :book) :alternative-map))

;; 2) Declare multi-method 
(defmulti normalize-book get-book-format)

;; 3) Implementation
(defmethod normalize-book :vector-book [book]
  {:title (first book) :author (second book)})

(defmethod normalize-book :standard-map [book]
  book)

(defmethod normalize-book :alternative-map [book]
  {:title (:book book) :author (:by book)})

(normalize-book {:title "Harry Potter" :author "JK Rowling"})
(normalize-book {:title "Harry Potter" :by "JK Rowling"})
(normalize-book ["Harry Potter" "JK Rowling"])

;; Recursion

(def albums
  [{:title "Lilac" :streams 1000000}
   {:title "Red" :streams 2000000}
   {:title "Plus" :streams 3000000}])

(defn sum-streams
  ([albums] (sum-streams albums 0))
  ([albums total]
   (if (empty? albums)
     total
     (sum-streams ;; This is bad because we are taking up more stack each recursion
      (rest albums)
      (+ total (:streams (first albums)))))))

(sum-streams albums)

;; Optimizing with tail call optimization
;; *recur is type of loop
(defn sum-streams-recur
  ([albums] (sum-streams-recur albums 0))
  ([albums total]
   (if (empty? albums)
     total
     (recur ;; Using recur to avoid accumulating stack frames
      (rest albums)
      (+ total (:streams (first albums)))))))


;; Optimizing with loop
;; * loop 
(defn sum-streams-loop [albums]
  (loop [albums albums total 0] ;; bounding albums & 0 to their respective default values
    (if (empty? albums)
      total
      (recur ;; Using recur to avoid accumulating stack frames
       (rest albums)
       (+ total (:streams (first albums)))))))


;; An even simpler way to sum
(defn sum-streams-apply [albums]
  (apply + (map :streams albums)))

;; Stackoverflow
(defn fibonacci
  [num]
  (if (= num 0) 0)
  (if (= num 1) 1)
  (+ (fibonacci (- num 2)) (fibonacci (- num 1))))

(fibonacci 2)

;; Docstring
;; Documentation 

(defn average
  "Return average of a and b" ;; Just insert string on first line of function
  [a b]
  (/ (+ a b) 2.0))

(average 100 80)

;; Pre and Post Conditions
;; Provides natural choke points
(defn straw-hat-crew [crew-member]
  ;; Simply add the :pre keyword in a map
  {:pre [(:name crew-member) (:devil-fruit crew-member)]} ;; remember we checking for truthy values here
  (println crew-member))

;; Post handles what happend after pre is triggered
(defn straw-hat-crew [crew-member]
  {:pre [(:name crew-member) (:devil-fruit crew-member)]
   :post [(boolean? %)]} ;; % is used as stand-in for the return value of pre
  (println crew-member))


;; Let
;; Encapsulated variable. def is global.

(defn compute-damage [strength attack is-weak]
  (let [modified-damage 1] ;; modified-damage is encpsulated within parentheses
    (if is-weak
      (* modified-damage 2))
    (+ modified-damage (* strength attack))))


;; if-let
(defn uppercase-author [book]
  (if-let [author (:author book)] ;; assign :author to author if it exists
    (.toUpperCase author) ;; then evaluate statement with assigned variable
    "NO AUTHOR")) ;; else assign to something else

;; when-let (pretty much the same)
(defn uppercase-author [book]
  (when-let [author (:author book)]
    (.toUpperCase author)))

;; def - and its magic
;; basically globally variables
;; is mutable unlike many things in Clojure

(def weather "mostly cloudy")
#'weather

(def tomorrow-weather #'weather)
#'tomorrow-weather

;; binding
;; Like let but it creats a dynamically scoped binding
;; let creates a lexically scoped immutable binding (only accessible within parens)

(def ^:dynamic some-var 0) ;; Set some-var to 0

;; Returns 1
(binding [some-var 1] (var-get #'some-var))

;; Assign some-var to 1  
;; Returns 0 because the some-var is not the same some-var declared with let 
(let [some-var 1] (var-get #'some-var))

;; Namespaces
;; vars live in namespaces
;; namespaces are like a big lookup table

;; Clojure creates a user namespace by default 
(def user-namespace-var "hello world")

;; Creation
(ns catalog)

;; Notice how every subsequent var is created in the catalog namespace
;; Even variables above are created in the catalog namespace
(def discount-rate 0.5)

(defn discount-price [book]
  (* (- 1.0 discount-rate) (:price book)))

(ns album)
(defn discount-price [album]
  (* (- 1.0 0.1) (:price album)))

;; Using specified namespace
(println (catalog/discount-price {:title "Getting Clojure" :price 20.00}))
(println (album/discount-rate {:title "Lilac" :price 50.00}))

;; Loading namespaces in Clojure
;; Using clojure.data as example

(require 'clojure.data)
(clojure.data/diff ["1" "2" "3"] ["1" "2" "4"])