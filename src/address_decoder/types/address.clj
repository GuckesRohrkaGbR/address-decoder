(ns address-decoder.types.address
  (:require [clojure.string :as str]))

(defrecord address [street number co zip city state country po-box po-zip po-city company-zip company-city])

(defn make
  ([]
   (address. "" "" "" "" "" "" "" "" "" "" "" ""))

  ([street, number, zip, city]
   (address. street number "" zip city "" "" "" "" "" "" ""))

  ([street number co zip city state country]
   (address. street number co zip city state country "" "" "" "" ""))

  ([po-box po-zip po-city state country]
   (address. "" "" "" "" "" state country po-box po-zip po-city "" "")))

(defn- trim-all
  [vector]
  (map #(.trim %) vector))

(defn- split-at-horiz-ws
  [segment]
  (case segment
    nil []
    (str/split
      (str/trim segment)
      #"\h+")))

(defn- split-segments
  [segments]
  (case segments
    nil []
    (trim-all
      (str/split segments #"[^\h\da-zA-Z\.\-_]+"))))

(defn- tokenize
  [address-string]
  (flatten
    (map
      split-at-horiz-ws
      (split-segments address-string))))

(defn parse
  "Parses addresses of all forms"
  [address-string]
  (apply make (tokenize address-string)))