(ns address-decoder.types.contact-name
  (:require [clojure.string :as str]))

(defrecord contact-name [given-names surnames organization-name])

(defn make
  ([]
   (contact-name. "" "" ""))

  ([given-names surnames]
   (contact-name. given-names surnames ""))

  ([organization-name]
   (contact-name. "" "" organization-name))

  ([organization-name given-names surnames]
   (contact-name. given-names surnames organization-name)))

(def org-name-elements
  ["GbR" "GmbH" " OHG" " KG" "eG" "e.V" "UG" "VVaG"])

(defn- is-organization-name
  [name-string]
  (some
    #(str/includes? name-string %)
    org-name-elements))

(defn- parse-person-name
  [name-string]
  (apply make (str/split name-string #"\s(?=[\S]*$)")))

(defn parse
  "parses names"
  [name-string]
  (cond
    (is-organization-name name-string) (make name-string)
    :else (parse-person-name name-string)))