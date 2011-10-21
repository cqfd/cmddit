(ns cmddit.models.commit
  (:require [somnium.congomongo :as mongo]))

(defn all []
  (mongo/fetch :commits))

(defn create [{:keys [username message]}]
  (mongo/insert! :commits
                 {:username username
                  :message message
                  :timestamp (.getTime (new java.util.Date))}))

(defn get-by-id [id]
  (mongo/fetch-by-id :commits (mongo/object-id id)))
