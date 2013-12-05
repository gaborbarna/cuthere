(ns cuthere.utils
  (:gen-class))


(defmacro dbg[x] `(let [x# ~x] (println "dbg:" '~x "=" x#) x#))
