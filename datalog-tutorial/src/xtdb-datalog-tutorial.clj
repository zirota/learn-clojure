(require '[xtdb.api :as xt]
         '[data])

;; Starts node without options
(def xtdb-node
  (xt/start-node {}))

;; Load data onto xtdb via submit-tx
(xt/submit-tx xtdb-node (for [doc data/database]
                            [:xtdb.api/put doc]))

;; sync ensures that nodes are fully indexed
(xt/sync xtdb-node)

;; Querying using xt/q

;; In simple terms:
;; Find all movie titles in the database
;; SELECT title WHERE :movie/title NOT NULL
(xt/q (xt/db xtdb-node)
      '{:find [title]
        :where [[_ :movie/title title]]})


;; We can declare a helper function to reduce visual clutter
(defn q [query & args]
  (apply xt/q (xt/db xtdb-node) query args))

(q '{:find [title]
     :where [[_ :movie/title title]]})

;; Find the id of the document where the person's name is "Alan Rickman"
(q '{:find [e] ;; Notice how xtdb will always return an entity id
     :where [[e :person/name "Alan Rickman"]]})

;; Find the entity ids of movies made in 1987
(q '{:find [e]
     :where [[e :movie/year 1987]]})

;; Find the entity-id and titles of movies in the database
(q '{:find [entity-id title]
     :where [[entity-id :movie/title title]]})

;; Find the name of all people in the database
(q '{:find [name]
     :where [[_ :person/name name]]})

;; Data Patterns
;; Combine queries like using SQL logical operator (OR AND)

;; Find all movie titles in the year 1987
;; e here acts as a logic variable, this simply tells the query engine to bind the results to the same value
;; it can be any variable excepter underscore
(q '{:find [title]
     :where [[e :movie/title title] ;; Take note that sequence does not matter
             [e :movie/year 1987]]})

;; Find all movie titles made in 1985
(q '{:find [title]
     :where [[e :movie/title title]
             [e :movie/year 1985]]})

;; Find the year that Alien was release
(q '{:find [year]
     :where [[e :movie/title "Alien"]
             [e :movie/year year]]})

;; Find the name of the person that directed RoboCop
(q '{:find [name]
     :where [[m :movie/title "RoboCop"]
             [m :movie/director d]
             [d :person/name name]]})

;; Find directors who have directed Arnold Schwarzenegger in a movie
(q '{:find [directors]
     :where [[p :person/name "Arnold Schwarzenegger"]
             [m :movie/cast p]
             [m :movie/director d]
             [d :person/name directors]]})

;; Parameterized Query
;; Its like SQL prepared statements
;; Using the :in keyword we use a tuple to represent a sqeuence of input variables
;; e.g. :in [[name age]]

;; Inputs
;; [[ ]] - Tuples, used when you want to destructure an input (e.g. [[ name age ]])
;; [[ ...]] - Collections, used to destrucuture an input and implement a logical OR
;; [ _ [[]]] - Relations, used to join external relations

;; Find movie title by year
(q '{:find [title]
     :in [year]
     :where [[e :movie/year year]
             [e :movie/title title]]}
   1989)

;; Given list of movie titles, find the title and the year that movie was released
(q '{:find [title year]
     :in [[title ...]] ;; Note the ... to represent a list
     :where [[m :movie/title title]
             [m :movie/year year]]}
   ["Lethal Weapon" "Lethal Weapon 2" "Lethal Weapon 3"])

;; Find all the movie titles where the actor and the director has worked together
(q '{:find [movie-title]
     :in [actor director]
     :where [[p :person/name actor]
             [d :person/name director]
             [m :movie/cast p]
             [m :movie/director d]
             [m :movie/title movie-title]]}
   "Michael Biehn"
   "James Cameron")

;; Given an actor name and a relation with movie-title/rating, 
;; finds the movie titles and corresponding rating for which that actor was a cast member.

(q '{:find [title rating]
     :in [name [[title rating]]]
     :where [[p :person/name name]
             [m :movie/cast p]
             [m :movie/title title]]}
   "Mel Gibson"
   [["Die Hard" 8.3]
    ["Alien" 8.5]
    ["Lethal Weapon" 7.6]
    ["Commando" 6.5]
    ["Mad Max Beyond Thunderdome" 6.1]
    ["Mad Max 2" 7.6]
    ["Rambo: First Blood Part II" 6.2]
    ["Braveheart" 8.4]
    ["Terminator 2: Judgment Day" 8.6]
    ["Predator 2" 6.1]
    ["First Blood" 7.6]
    ["Aliens" 8.5]
    ["Terminator 3: Rise of the Machines" 6.4]
    ["Rambo III" 5.4]
    ["Mad Max" 7.0]
    ["The Terminator" 8.1]
    ["Lethal Weapon 2" 7.1]
    ["Predator" 7.8]
    ["Lethal Weapon 3" 6.6]
    ["RoboCop" 7.5]])

;; Predicates

(q '{:find [title]
     :where [[m :movie/title title]
             [m :movie/year year]
             [(< year 1984)]]})

;; You can use any Clojure function in XTDB
(q '{:find [name]
     :where [[p :person/name name]
             [(clojure.string/starts-with? name "M")]]})

;; Find movies older than a certain year (inclusive)
(q '{:find [title]
     :in [year]
     :where [[m :movie/year movie-year]
             [(>= movie-year year)]
             [m :movie/title title]]}
   1984)

;; Find actors older than Danny Glover
(q '{:find [actor]
     :where [[dg :person/name "Danny Glover"]
             [dg :person/born bday]
             [p :person/born p-bday]
             [p :person/name actor]
             [(>= p-bday bday)]]})

;; Find movies newer than year (inclusive) and has a rating higher than the one supplied
(q '{:find [title]
     :in [year rating [[title r]]]
     :where [[m1 :movie/year movie-year]
             [m1 :movie/title title]
             [(<= year movie-year)]
             [(< rating r)]]}
   1990
   8.0
   [["Die Hard" 8.3]
    ["Alien" 8.5]
    ["Lethal Weapon" 7.6]
    ["Commando" 6.5]
    ["Mad Max Beyond Thunderdome" 6.1]
    ["Mad Max 2" 7.6]
    ["Rambo: First Blood Part II" 6.2]
    ["Braveheart" 8.4]
    ["Terminator 2: Judgment Day" 8.6]
    ["Predator 2" 6.1]
    ["First Blood" 7.6]
    ["Aliens" 8.5]
    ["Terminator 3: Rise of the Machines" 6.4]
    ["Rambo III" 5.4]
    ["Mad Max" 7.0]
    ["The Terminator" 8.1]
    ["Lethal Weapon 2" 7.1]
    ["Predator" 7.8]
    ["Lethal Weapon 3" 6.6]
    ["RoboCop" 7.5]])


;; Transformation Functions
;; Pure side-effect functions which can be used in queries as "function expression" predicates
;; to transform values and bind their results to new logic variables


;; Calculating approximate age of a person using the :db.type/instant
(defn age [^java.util.Date birthday ^java.util.Date today]
  (quot (- (.getTime today)
           (.getTime birthday))
        (* 1000 60 60 24 365)))

(q '{:find [age]
     :in [name today]
     :where [[p :person/name name]
             [p :person/born born]
             [(user/age born today) age]]}
   "Tina Turner"
   (java.util.Date.))

;; Find people by age. Use the function user/age to find the names of people,
;; given their age and a date representing 'today'.
(q '{:find [name]
     :in [age today]
     :where [[p :person/name name]
             [p :person/born born]
             [(user/age born today) p-age]
             [(== p-age age)]]}
   60
   (java.util.Date.))

;; Find the names of people younger than Bruce Willis and their corresponding age.
(q '{:find [name age]
      :in [today]
      :where [[p :person/name name]
              [p :person/born p-bday]
              [(user/age p-bday today) age]
              [bw :person/name "Bruce Willis"]
              [bw :person/born bw-bday]
              [(user/age bw-bday today) bw-age]
              [(< age bw-age)]]}
   (java.util.Date.))

;; Aggregrates
;; count, sum, max etc. (Like SQL)
;; Added to :find

;; Count the number of movies in the database
(q '{:find [(count movies)]
     :where [[movies :movie/title]]})

;; Find birthday of oldest person in the database
(q '{:find [(min date)]
     :where [[p :person/born date]]})

;; Give a collection actors and ratings data. Find the average rating for each actor.
;; Query should return the actor name and the avg rating.
(q '{:find [name (avg rating)]
     :in [[name ...] [[title rating]]]
     :where [[actor :person/name name]
             [m :movie/cast actor]
             [m :movie/title title]]}
   ["Alan Rickman" "Mel Gibson"]
   [["Die Hard" 8.3]
    ["Alien" 8.5]
    ["Lethal Weapon" 7.6]
    ["Commando" 6.5]
    ["Mad Max Beyond Thunderdome" 6.1]
    ["Mad Max 2" 7.6]
    ["Rambo: First Blood Part II" 6.2]
    ["Braveheart" 8.4]
    ["Terminator 2: Judgment Day" 8.6]
    ["Predator 2" 6.1]
    ["First Blood" 7.6]
    ["Aliens" 8.5]
    ["Terminator 3: Rise of the Machines" 6.4]
    ["Rambo III" 5.4]
    ["Mad Max" 7.0]
    ["The Terminator" 8.1]
    ["Lethal Weapon 2" 7.1]
    ["Predator" 7.8]
    ["Lethal Weapon 3" 6.6]
    ["RoboCop" 7.5]])

;; Rules
;; Abstract reusable parts in the query

;; Comparison no rules vs rules
(q '{:find [name]
     :where [[p :person/name name]
             [m :movie/cast p]
             [m :movie/title "The Terminator"]]})

(q '{:find [name]
     :where [(get-movie-actors name "The Terminator")]
     :rules [[(get-movie-actors name title)
             [p :person/name name]
             [m :movie/cast p]
             [m :movie/title title]]]})

;; Write a rule (movie-year title year)
;; where title is the title of some movie and year is that movie's release year
(q '{:find [title]
     :where [(movie-year title 1991)]
     :rules [[(movie-year title year)
             [m :movie/year year]
             [m :movie/title title]]]})

;; Two people are friends if they have worked together in a movie. 
;; Write a rule (friends p1 p2) where p1 and p2 are person entities. 
;; Try with a few different name inputs to make sure you got it right.
;; There might be some edge cases here.
(q '{:find [friend]
     :in [name] 
     :where [[p1 :person/name name]
             (friends p1 p2)
             [p2 :person/name friend]]
     :rules [[(friends p1 p2)
              [m :movie/cast p1]
              [m :movie/cast p2] ;; When friend is casted in the same movie
              [(not= p1 p2)]] ;; We dont want to return ownself as friend
             [(friends p1 p2) ;; Notice how we can stack rules to create logical OR
              [m :movie/cast p1]
              [m :movie/director p2] ;; When friend is the director of the movie that they are casted in
              [(not= p1 p2)]]
             [(friends p1 p2)
              [m :movie/director p1] ;; When friend is cast of the movie they are directing
              [m :movie/cast p2] 
              [(not= p1 p2)]]]}
   "Alan Rickman")


