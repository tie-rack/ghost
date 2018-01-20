(defproject ghost "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.908"]
                 [org.slf4j/slf4j-nop "1.7.25"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.2"]]

  :plugins [[lein-cljsbuild "1.1.5"]
            [deraen/lein-sass4clj "0.3.1"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "resources/public/css/compiled"
                                    "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :server-port 3450}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :sass {:source-paths ["src/scss"]
         :target-path "resources/public/css/compiled"
         :output-style :compressed}

  :aliases {"dev" ["do" "clean"
                   ["pdo"
                    ["sass4clj" "auto"]
                    ["figwheel" "dev"]]]
            "build" ["do" "clean"
                     ["sass4clj" "once"]
                     ["cljsbuild" "once" "min"]]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.4"]
                   [figwheel-sidecar "0.5.13"]
                   [com.cemerick/piggieback "0.2.2"]]

    :plugins      [[lein-figwheel "0.5.13"]
                   [lein-doo "0.1.8"]
                   [lein-pdo "0.1.1"]]}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "ghost.core/mount-root"}
     :compiler     {:main                 ghost.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            ghost.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:main          ghost.runner
                    :output-to     "resources/public/js/compiled/test.js"
                    :output-dir    "resources/public/js/compiled/test/out"
                    :optimizations :none}}]}

  :license {:name "Mozilla Public License"
            :url "https://www.mozilla.org/media/MPL/2.0/index.txt"})
