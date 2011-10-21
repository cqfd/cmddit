(ns cmddit.views.welcome
  (:require [cmddit.views.common :as common]
            [cmddit.models.user :as user]
            [cmddit.secrets :as secrets]
            [noir.session :as session]
            [noir.response :as resp])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers))

(defpartial login-fields [{:keys [username password]}]
            (label "username" "username: ")
            (text-field "username" username)
            (label "password" password)
            (password-field "password" password))

(defpartial user-item [{:keys [username]}]
            [:li.user username])

(defpage "/welcome" []
         (common/layout
           [:p "welcome to cmddit"]))

(defpage [:get "/login"] {:as user}
         (common/layout
           (form-to [:post "/login"]
                    (login-fields user)
                    (submit-button "login"))))

(defpage [:post "/login"] {:keys [username password]}
         (if (user/authenticate username password)
           (do
             (session/put! :username username)
             (resp/redirect "/session"))
           (do
             (resp/redirect "/login"))))

(defpage [:get "/register"] {:as user}
         (common/layout
           (form-to [:post "/register"]
                    (login-fields user)
                    (submit-button "register"))))

(defpage [:post "/register"] {:keys [username password]}
         (user/register username password)
         (resp/redirect "/users"))

(defpage [:get "/users"] []
         (common/layout
           [:ul#users
             (map user-item (user/all))]))

(defpage [:get "/session"] []
         (common/layout
           [:p "Hi " (session/get :username) "!"]))
