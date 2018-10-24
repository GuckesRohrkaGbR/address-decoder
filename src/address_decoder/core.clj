(ns address-decoder.core
  (:require [address-decoder.types.contact :as contact]))

(defn parse
  "Parses address signatures"
  [input-string]
  (contact/parse input-string))

