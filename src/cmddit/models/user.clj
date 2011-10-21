(ns cmddit.models.user
  (:require [somnium.congomongo :as mongo]
            [noir.session :as session]))

(defn get-by-username [username]
  (mongo/fetch-one :users
                   :where {:username username}))

(defn authenticate [username password]
  (= password (:password (get-by-username username))))

(defn register [username password]
  (mongo/insert! :users
                 {:username username
                  :password password}))

(defn all []
  (mongo/fetch :users))
