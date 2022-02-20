(ns server
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test]
            [domain :as domain]
            [api :as api]))

;; Interceptor
;; Pedestal uses interceptors to route requests
;; Two basic keys :enter & :leave
;; The enter function is called down the queue
;; The leave function is called up the the queue

(def echo
  {:name :echo ;; :name is the name of the interceptor
   :enter ;; An :enter keyword in this map is how Pedestal knows which functions to call
   (fn [context]
     (let [request (:request context)
           response (api/ok context)]
       (println "path-params" (:path-params request))
       (println "body" (:body request))
       (assoc context :response response)))})

(def entity-created
  {:name :entity-render
   :leave
   (fn [context]
     (if-let [item (:result context)]
       (assoc context :response (api/created item))
       context))})

(def entity-render
  {:name :entity-render
   :leave
   (fn [context]
     (if-let [item (:result context)]
       (assoc context :response (api/ok item))
       context))})

;; Each route needs to have a unique route-name
;; Since we use the echo interceptor everywhere we have to explicitly declare a route-name
;; The default route-name is name of the last interceptor in the vector

(def routes
  (route/expand-routes
   ;; interceptors in the vector :enter functions get evaluated left to right
   ;; interceptors in the vector :leave functions get evaluated right to left
   #{["/todo"                    :post   [entity-created domain/list-create]] 
     ["/todo"                    :get    echo :route-name :list-query-form]
     ["/todo/:list-id"           :get    [entity-render domain/list-view]]
     ["/todo/:list-id"           :post   [entity-created domain/list-item-create]]
     ["/todo/:list-id/:item-id"  :get    [entity-render domain/list-item-view]]
     ["/todo/:list-id/:item-id"  :put    echo :route-name :list-item-update]
     ["/todo/:list-id/:item-id"  :delete echo :route-name :list-item-delete]}))

(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8890})

(defn start []
  (http/start (http/create-server service-map)))

(defonce server (atom nil))

(defn start-dev []
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false)))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))

(comment
  (start-dev)
  (restart)
  (require :reload 'server)
  (require :reload 'repository)
  (require :reload 'domain)

  ;; Poking the routes
  (test/response-for (:io.pedestal.http/service-fn @server) :get "/todo")
  (dissoc (test/response-for (:io.pedestal.http/service-fn @server) :get "/todo") :body)

  ;; Testing for 404
  (dissoc (test/response-for (:io.pedestal.http/service-fn @server) :get "/helloworld") :body)

  ;; Query path parameters
  (test/response-for (:io.pedestal.http/service-fn @server) :get "/todo/l21849")
  (test/response-for (:io.pedestal.http/service-fn @server) :get "/todo/l21849/1234")

  ;; Post body
  (test/response-for (:io.pedestal.http/service-fn @server) :post "/todo")
  (test/response-for (:io.pedestal.http/service-fn @server) :post "/todo?name=Hazzen")
  (test/response-for (:io.pedestal.http/service-fn @server) :post "/todo/l21849")

  ;; Query Database
  @repository/database
  ;;
  )