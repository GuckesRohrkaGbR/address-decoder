(ns address-decoder.core
  (:require [address-decoder.types.contact :as contact]))

(defn parse
  "Parses address signatures"
  [inputString]
  (contact/parse inputString))

