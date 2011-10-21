(ns cmddit.server
  (:require [noir.server :as server]
            [cmddit.models :as models]))

(server/load-views "src/cmddit/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (models/init)
    (server/start port {:mode mode
                        :ns 'cmddit})))

