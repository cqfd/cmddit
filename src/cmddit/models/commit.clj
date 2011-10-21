(ns cmddit.models.commit
  (:require [cmddit.models.user :as user]
            [somnium.congomongo :as mongo]))

(defn all []
  (mongo/fetch :commits))

(defn feed [user-ids]
  (let [user-ids-set (into #{} user-ids)
        usernames (map user/id->username user-ids-set)
        commits (mongo/fetch :commits
                             :where {:username {:$in usernames}})]
    (sort-by :timestamp > commits)))

(defn create [{:keys [username message]}]
  (mongo/insert! :commits
                 {:username username
                  :message message
                  :timestamp (.getTime (new java.util.Date))}))

(defn get-by-oid [oid]
  (mongo/fetch-by-id :commits oid))

(defn get-by-id [id]
  (get-by-oid (mongo/object-id id)))
