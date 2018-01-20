;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.

(ns ghost.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [ghost.subs :as subs]
            [ghost.events :as events]
            [clojure.string :as str]))

(defn set-media-handler [media]
  (fn [e]
    (let [file (aget (.. e -target -files) 0)
          file-reader (js/FileReader.)]
      (set! (.-onload file-reader)
            (fn [e]
              (reset! media (.. e -target -result))))
      (.readAsDataURL file-reader file))))

(defn compose [name]
  (let [media (r/atom nil)]
    (fn [name]
      [:div.compose
       [:textarea#content.textarea]
       [:div.compose-buttons
        (if @media
          [:span.faded.smaller
           "Picture attached"
           [:button.btn-link {:on-click
                              #(reset! media nil)}
            "remove"]]
          [:span
           [:button.btn
            {:on-click
             (fn [_]
               (-> js/document
                   (.getElementById "upload")
                   .click))}
            "Add picture"]
           [:input#upload.hidden
            {:type "file"
             :accept "image/*"
             :on-change (set-media-handler media)}]])
        [:button.btn
         {:on-click
          #(let [textarea (.getElementById js/document "content")
                 content (.-value textarea)]
             (when (or @media (not (str/blank? content)))
               (re-frame/dispatch
                [::events/post
                 {:name name
                  :content content
                  :media @media}]
                (set! (.-value textarea) "")
                (reset! media nil))))}
         "Post"]]])))

(defn post [{:keys [id name content media] :as post}]
  [:div.post {:key id}
   [:div
    [:div.author "@" name]
    [:div.post-content
     (when-not (str/blank? content)
       [:div.content content])
     (when media
       [:img.media.width-auto {:src media}])]]
   [:div.post-buttons
    [:button.btn
     {:on-click #(re-frame/dispatch [::events/repost post])}
     "repost"]
    [:button.btn
     {:on-click #(re-frame/dispatch [::events/delete-post id])}
     "delete"]]])

(defn logged-in [name]
  (let [posts (re-frame/subscribe [::subs/posts])]
    [:div
     [:div "@" name
      [:button.btn-link {:on-click
                         #(re-frame/dispatch
                           [::events/logout])}
       "Log out"]]
     [compose name]
     [:div.posts
      (map post @posts)]]))

(defn about []
  [:div.about
   [:h1 "Ghost"]
   [:p
    "Ghost is a social network without the social part or the network part. "
    "Since there's no one else, you can have any name you want. Just log in."]
   [:p
    "Nothing you post goes out to the internet. "
    "Nothing gets written to a database. "
    "Nothing sticks around. "
    "Everything disappears when you log out or close the page."]
   [:p
    "Relax. You're on Ghost."]
   [:p.faded
    "Ghost was made by Christopher Shea, who you can email I guess: "
    [:a.faded {:href "mailto:cmshea@gmail.com"} "cmshea@gmail.com"]]])

(defn logged-out []
  [:div
   [:input#username]
   [:button.btn
    {:on-click
     #(re-frame/dispatch
       [::events/login (.. js/document
                           (getElementById "username")
                           -value)])}
    "Log in"]
   [about]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.main
     (if @name
       [logged-in @name]
       [logged-out])]))
