(ns domain
  (:require
   [repository :as repo]))

;; (def db-interceptor
;;   {:name :database-interceptor
;;    :enter
;;    (fn [context]
;;      (update context :request assoc :database @repo/database))
;;    :leave
;;    (fn [context]
;;      (if-let [[op & args] (:tx-data context)]
;;        (do
;;          (println "op: " op)
;;          (println "args: " args)
;;          (apply swap! repo/database op args)
;;          (assoc-in context [:request :database] @repo/database)
;;          (assoc context :result {:tx-data args}))
;;        context))})

(def list-create
  {:name :list-create
   :enter
   (fn [context]
     (let [name       (get-in context [:request :query-params :name] "Unnamed List")
           make-list-result (repo/create-list name)]
       (assoc context :result make-list-result)))})

(def list-view
  {:name :list-view
   :enter
   (fn [context]
     (if-let [list-id (get-in context [:request :path-params :list-id])]
       (if-let [the-list (repo/find-list-by-id list-id)]
         (assoc context :result the-list))
       context))})

(def list-item-view
  {:name :list-item-view
   :leave
   (fn [context]
     (if-let [list-id (get-in context [:request :path-params :list-id])]
       (if-let [item-id (get-in context [:request :path-params :item-id])]
         (if-let [item (repo/find-list-item-by-ids (get-in context [:request :database]) list-id item-id)]
           (assoc context :result item)
           context)
         context)
       context))})

(def list-item-create
  {:name :list-item-create
   :enter
   (fn [context]
     (if-let [list-id (get-in context [:request :path-params :list-id])]
       (let [name       (get-in context [:request :query-params :name] "Unnamed Item")
             create-list-item-result (repo/create-list-item list-id name)]
         (assoc context :result create-list-item-result))
       context))})
