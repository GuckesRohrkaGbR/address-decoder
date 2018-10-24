(ns address-decoder.types.contact
  (:require [clojure.string :as str])
  (:require [address-decoder.types.contact-name :as contact-name])
  (:require [address-decoder.types.address :as address]))

(defrecord contact [contact-name address phone])

(defn make
  ([]
   (contact. nil nil nil))

  ([contact-name address phone]
   (contact. contact-name address phone)))

(defn- split-name-and-address
  [contact-string]
  (case contact-string
    nil []
    (let [lines (str/split-lines contact-string)]
      [(first lines) (str/join "\n" (rest lines))])))

(defn parse
  "parses complete strings like post addresses or mail
  signatures into machine readable contact data"
  [contact-string]
  (let [split (split-name-and-address contact-string)]
    (let [cont-name (contact-name/parse (first split))
          addr (address/parse (nth split 1))]
      (make cont-name addr nil))))




