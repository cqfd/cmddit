(ns cmddit.views.users
  (:require [cmddit.views.common :as common]
            [cmddit.models.user :as user]
            [cmddit.models.commit :as commit]
            [noir.session :as session]
            [noir.response :as resp])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers))

(defpartial user-item [{:keys [username]}]
            [:li.user username])

(defpage [:get "/users"] []
         (common/layout
           [:ul#users
             (map user-item (user/all))]))

(defpage [:get "/users/:id/following"] {:keys [id]}
         (common/layout
           [:ul#users
            (map user-item (user/following id))]))

(defpage [:get "/users/:id/followees"] {:keys [id]}
         (common/layout
           [:ul#users
            (map user-item (user/followees id))]))

(defpage [:post "/users/:id/commits"] {:keys [id message]}
         (let [c (commit/create 
                        {:username (user/id->username id)
                         :message message})]
           (resp/json (assoc c :_id (str (:_id c))))))

(defpage [:get "/users/:id/feed"] {:keys [id]}
         (let [feed (commit/feed (conj (user/following-ids id) id))]
           (resp/json (map #(dissoc % :_id) feed))))

(defpage [:put "/users/:from-id/following/:to-id"] {:keys [from-id to-id]}
         (let [from (user/get-by-id from-id)
               to (user/get-by-id to-id)]
           (if (and from to)
             (do
               (user/create-link from-id to-id)
               (resp/json {:status "ok"}))
             (resp/json {:error "unable to create link"}))))
