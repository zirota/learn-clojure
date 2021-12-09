(ns intermediate)

;; Sequence
;; Can be considered as a wrapper like iterable or collection in other languages

(def name-seq (seq ["Hazzen" "Chua" "Tien" "Yuu"]))
name-seq ;; Note that even with round brackets its a seq not a list

(seq '("Hazzen" "Chua" "Tien" "Yuu"))

;; seq on map. creates sequence of key value pair
;; Note that the order is not guaranteed
(seq {:title "Palette" :singer "IU" :published 2019})

;; does nothing
(seq (seq {:title "Palette" :singer "IU" :published 2019}))

;; Adding to sequence - front
(cons "zirota" name-seq)

(first name-seq)
(rest name-seq)
(next name-seq) ;; same as rest but returns nil on empty seq

(reverse name-seq)

;; sort calls the java sort method which is a Dual-Pivot Quicksort O(n log(n))
(sort name-seq)
(sort (seq [47 38 96 74 34]))

;; partition creates sequence of sequences
;; it returns sequences on first come first serve basis.
;; Omit values if unable to fill up the small sequence
(partition 3 name-seq)
(partition 2 name-seq)

(def song-titles ["Coin" "2021"])
(def singers '("Lee" "Ji" "Eun" "IU"))

;; mixing in alternate values to length of shorter seq
;; first values starts from first value of first seq
(interleave song-titles singers)
(interleave singers song-titles)

;; sprinkle separator value between elements of sequence
(interpose "," name-seq)
(interpose song-titles singers)

;; filter
;; returns another seq based on condition
(filter neg? '(1 -100 -8 47 38)) ;; neg is a built-in function for negative values

(defn is-cheap? [financials]
  (when (<= (:pe financials) 40.00)
    financials))

(def financials
  [{:pe 43 :ticker "AMD"}
   {:pe 92.74 :ticker "NVDA"}
   {:pe 188.98 :ticker "SQ"}
   {:pe 27.27 :ticker "GOOGL"}
   {:pe 36.46 :ticker "MSFT"}])

(filter is-cheap? financials)

;; Kinda same JS map
(map :ticker financials)

;; comp takes a function an apply it to every single input function
;; count is a function to count letters in a string
(map (comp count :ticker) financials)

;; Kinda same as JS reduce
(def numbers [38 47 96])

;; reduce [fn initial-value seq]
(reduce (fn [a b] (+ a b)) 0 [38 47 96])


;; sort-by [:keyword seq]
(sort-by :pe financials)

;; take
(take 2 (reverse (sort-by :pe financials)))

;; Reading and Writing to file

;; reading with slurp
(slurp "lorem-ipsum.txt")


;; Lazy Sequences

;; repeat
;; create same value everytime
;; repeated sequences does not end
;; but are lazily created
(def stonks "stonks only go up")

(def repeated-stonks (repeat stonks))

(first repeated-stonks)
(nth repeated-stonks 10)

;; take first N items
(take 20 repeated-stonks)

;; cycle
;; repeats a sequence
(take 7 (cycle [5 4 3 2 1]))

;; iterate
;; iterate applies function to the initial value
;; remember that these are lazily generated
(def iterate-numbers (iterate inc 1))
(take 20 iterate-numbers)

(def trilogy (map #(str "Matrix: " %) [1 2 3]))
trilogy

(defn lazy-numbers []
  (println "I am lazy!")
  [1 2 3])

;; lazy-seq will not be generated unless pulling it
(def s (lazy-seq (lazy-numbers)))

(first s)

;; Note to not directly print infinite sequence
;; (println repeated-stonks)

;; doall
;; simply forcing lazy sequence to execute
(doall trilogy)


;; Destructuring
;; works with any sequence, functions, map types
(def artists [:iu :abel])

(let [female (first artists)
      male (second artists)]
  (println "Female Artist:" female
           "and the Male artist:" male))

;; desctruturing - same thing in JS
(let [[female male] artists]
  (println "Female Artist:" female
           "and the Male artist:" male))

;; placeholder for ignoring items
(let [[_ male] artists] ;; we use square brackets regardless of seq
  (println "Male artist:" male))

(def pairs [[:monet :austen] [:beethoven :dickson]])

;; 2-layer destructuring
(let [[painter [composer]] pairs]
  (println "The painter is" painter)
  (println "The composer is" composer))

;; function params
(defn artists-description [[female male]]
  (str "Female: " female " Male: " male))

(artists-description [(first artists) (second artists)])

;; Destrcuture map
(def artist-map {:kpop :iu :r&b :abel})

(let [{kpop :kpop r&b :r&b} artist-map]
  (println "The kpop artist is: " kpop)
  (println "The r&b artist is: " r&b))

;; Combination
(def author {:name "John Bogel" :books [{:title "Little Book of Common Sense Investing" :published 2007}]})

(let [{name :name [book] :books} author]
  (println "The author is: " name)
  (println "One of the author's books is: " book))

(def romeo {:name "Romeo" :age 16 :gender :male})
;; :keys
;; special keyword to destructure map values
(defn character-desc [{:keys [name age gender]}]
  (str "Name: " name " age: " age " gender: " gender))

(character-desc romeo)

;; Mix and match
(defn character-desc [{:keys [name gender] age-in-years :age}]
  (str "Name: " name " age: " age-in-years " gender: " gender))
(character-desc romeo)

;; :or
;; Provides default values in case destructuring fails
(defn api-client
  "Some api client"
  [{:keys [host port db]
    :or {host "localhost", port 3306, db ""} ;; default values are filled
    :as opts}]) ;; Note that these are function params. :as keyword provides acces to map


;; Records
;; In summary, a speedier map with predefined keys
;; This is kinda like interfaces

;; The names are created as :keywords
(defrecord FictionalCharacter [name appears-in author])
(def xiao-ming (->FictionalCharacter "Xiao Ming" "Chinese Composition" "Hazzen"))
(def xiao-hua (assoc xiao-ming :name "Xiao Hua"))

(defrecord Employee [first-name last-name department])
(def hazzen (->Employee "Hazzen" "Chua" "ADA"))

;; Protocols
;; This is kinda like an abstract classes
;; Here we focus on the common functions

(defprotocol Person
  (full-name [this]) ;; this refers to current record we are working on. Necessary for functions. Generally placeholder.
  (greeting [this msg]) ;; if function takes params then this must come first
  (description [this]))

(defrecord FictionalCharacter [name appears-in author] ;; Note the camelcase for records & protocols
  Person
  (full-name [this] (:name this))
  (greeting [this msg] (str msg " " (:name this)))
  (description [this]
    (str (:name this) " character in " (:appears-in this))))

(defrecord Employee [first-name last-name department]
  Person
  (full-name [this] (str first-name " " last-name)) ;; Why are we using var directly here?
  (greeting [e msg] (str msg " " (:first-name e))) ;; Why need to use :keyword with local variable?
  (description [this]
    (str (:first-name this) " works in: " (:department this))))

(let [hazzen (->Employee "Hazzen" "Chua" "ADA")]
  (println (full-name hazzen))
  (println (greeting hazzen "Good Morning!"))
  (description hazzen))

(defprotocol Marketable
  (make-slogan [this]))

;; extend-protocol
;; Allows us to create new functions on existing protocol easily without manual edits

(extend-protocol Marketable ;; we need to do defprotocol for it to work
  Employee
  (make-slogan [e] (str (:first-name e) " is the BEST employee")) ;; notice how we use "e". Generally just placeholder
  FictionalCharacter
  (make-slogan [slogan] (str (:first-name slogan) " is the BEST character")))