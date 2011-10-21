(ns cmddit.models.user
  (:require [somnium.congomongo :as mongo]
            [noir.session :as session]))

(defn get-by-oid [oid]
  (mongo/fetch-by-id :users oid))

(defn get-by-id [id]
  (get-by-oid (mongo/object-id id)))

(defn get-by-username [username]
  (mongo/fetch-one :users :where {:username username}))

(defn all []
  (mongo/fetch :users))

(defn id->username [id]
  (:username (get-by-id id)))

(defn authenticate [username password]
  (= password (:password (get-by-username username))))

(defn register [username password]
  (mongo/insert! :users {:username username :password password}))

(defn create-link [from-id to-id]
  (let [from-oid (mongo/object-id from-id)
        to-oid (mongo/object-id to-id)]
    (mongo/update! :users {:_id from-oid} {:$addToSet {:following to-oid}})
    (mongo/update! :users {:_id to-oid} {:$addToSet {:followees from-oid}})))

(defn following [user-id]
  (let [following-oids (:following (get-by-id user-id))]
      (map get-by-oid following-oids)))

(defn following-ids [user-id]
  (map (comp str :_id) (following user-id)))

(defn followees [user-id]
  (let [followee-oids (:followees (get-by-id user-id))]
      (map get-by-oid followee-oids)))

(defn followee-ids [user-id]
  (map (comp str :_id) (followees user-id)))
