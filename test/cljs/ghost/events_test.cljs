;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.

(ns ghost.events-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [ghost.events :as events]))

(deftest post-test
  (let [post {:content "Hello"}
        db {:index 41
            :posts '()}
        db-after (events/post db [:events/post post])]
    (testing "assocs the index onto the post"
      (let [[{post-index :id}] (:posts db-after)]
        (is (= post-index 41))))
    (testing "adds the post to posts"
      (let [[{post-content :content}] (:posts db-after)]
        (is (= "Hello" post-content))))
    (testing "increments the index"
      (is (= 42 (:index db-after))))))
