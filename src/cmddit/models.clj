(ns cmddit.models
  (:require [somnium.congomongo :as mongo]))

(defn init []
  (mongo/mongo! :db "cmddit"))
