;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.

(ns ghost.events
  (:require [re-frame.core :as re-frame]
            [ghost.db :as db]))

(defn initialize-db [_ _]
  db/default-db)

(defn login [db [_ name]]
  (assoc db :name name))

(defn post [{:keys [index] :as db} [_ post]]
  (-> db
      (update :posts conj (assoc post :id index))
      (update :index inc)))

(defn delete-post [db [_ id]]
  (update db :posts (partial remove #(= id (:id %)))))

(re-frame/reg-event-db ::initialize-db initialize-db)
(re-frame/reg-event-db ::login login)
(re-frame/reg-event-db ::logout initialize-db)
(re-frame/reg-event-db ::post post)
(re-frame/reg-event-db ::delete-post delete-post)
(re-frame/reg-event-db ::repost post)
