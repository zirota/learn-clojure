(ns repository)

(defonce database (atom {}))

(defn- make-list [name]
  {:name  name
   :items {}})

(defn- make-list-item [name]
  {:name  name
   :done? false})

(defn list-item-add
  [dbval list-id item-id new-item]
  (if (contains? dbval list-id)
    (assoc-in dbval [list-id :items item-id] new-item)
    dbval))

(defn create-list [name]
  (let [list-id (str (gensym "l"))
        list (merge {:id list-id} (make-list name))]
    (swap! database assoc list-id list)
    list))

(defn create-list-item [list-id name]
  (if (contains? @database list-id)
    (let [item-id (str (gensym 1))
          list-item (merge {:id item-id} (make-list-item name))]
      (swap! database assoc-in [list-id :items item-id] list-item)
      list-item)
    ;; defaults to 404
    )
  
  ;; (let [db @database
  ;;       item-id (str (gensym "l"))
  ;;       list (merge {:id db-id} (make-list name))]
  ;;   (swap! database assoc db-id list)
  ;;   list)
  )

(defn find-list-by-id [id]
  (get @database id))

(defn find-list-item-by-ids [list-id item-id]
  (get-in @database [list-id :items item-id] nil))