(ns cmddit.views.commits
  (:require [cmddit.views.common :as common]
            [cmddit.models.user :as user]
            [cmddit.models.commit :as commit]
            [noir.session :as session]
            [noir.response :as resp])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers))

(defpartial commit-item [{:keys [username message]}]
            [:li.commit username ": " message])

(defpage "/commits" []
         (common/layout
           (map commit-item (commit/all))))

(defpage [:post "/commits"] {:keys [username message]}
         (if-let [commit-id (commit/create {:username username
                                            :message message})]
           (let [c (commit/get-by-id commit-id)]
             (resp/json
               (dissoc c :_id)))
           (resp/json
             {:error "failed to create new commit"})))

(defpage [:get "/commits/:id"] {:keys [id]}
         (if-let [c (commit/get-by-id id)]
           (resp/json
             (dissoc c :_id))
           (resp/json {:error "unable to find that commit"})))
