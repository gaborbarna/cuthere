(ns cuthere.config
  (:require
    [taoensso.timbre :as timbre]
    [clojure.tools.cli :refer [cli]]
    [clojure.edn :as edn]
    [clojure.core.strint :refer [<<]])
  (:gen-class))


(declare cfg)

(def env-variables ["PORT", "MONGOLAB_URI" "PWD"])

(defn safe-submap [m k]
  (let [sub-map (if (map? m) (or (m k) {}) {})]
    (if (map? sub-map) sub-map {})))

(defn merge-configs-2 [main-cfg cfg]
  ((fn ! [main-map map]
     (merge main-map
            (reduce (fn [acc [k v]]
                      (let [sub-main-map (safe-submap main-map k)
                            new-map (if (map? v)
                                      (! sub-main-map (merge sub-main-map v)) v)]
                        (assoc acc k new-map))) {} map))) main-cfg cfg))

(defn merge-configs [& cfgs]
  (reduce (fn [acc cfg] (merge-configs-2 acc cfg)) {} cfgs))

(defn get-env [env-variables]
  (reduce (fn [acc k] (let [v (System/getenv k)] (if v (assoc acc k v) acc)))
          {} env-variables))

(defn get-config [cli-conf]
  (let [env-conf (get-env env-variables)
        config-file (cli-conf :config-file)
        file-conf (edn/read-string (slurp config-file))]
    (timbre/info (<< "config file ~{config-file} loaded"))
    (merge-configs file-conf env-conf cli-conf)))

(defn parse-cli [args]
  (cli args
       "CU there app"
       ["-c" "--config-file" "config file location" :default "config/default.edn"]
       ["-d" "--[no-]dev" "start in development mode" :default false]
       ["-h" "--help" "show help" :flag true :default false]))

(defn get-cli [args & {:keys [msg show-help] :or {msg 'nil show-help false}}]
  (try
    (let [[opts extras banner] (parse-cli args)
          banner-extras (if msg (str msg "\n" banner) banner)]
      {:show-help (or show-help (opts :help)), :opts (dissoc opts :help),
       :extras extras, :banner banner-extras})
    (catch Exception e (get-cli [] :msg (.getMessage e) :show-help true))))

(defn def-cfg [cfg]
  (def cfg cfg))
