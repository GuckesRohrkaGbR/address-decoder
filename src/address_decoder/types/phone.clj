(ns address-decoder.types.phone)

(defrecord phone [country prefix number extension])

(declare prepend-zero)
(defn make
  ([]
   (phone. "" "" "" ""))

  ([prefix number]
   (phone. "" (prepend-zero prefix) number ""))

  ([country prefix number]
   (phone. country (prepend-zero prefix) number ""))

  ([country prefix number extension]
   (phone. country (prepend-zero prefix) number extension)))

;------------------ Helpers
(defn- prepend-zero [prefix]
  (if (.startsWith prefix "0")
    prefix
    (str "0" prefix)))

(defn- remove-leading-zero [prefix]
  (if (.startsWith prefix "0")
    (.substring prefix 1)
    prefix))

(defn- whitespace-first-item
  [vector]
  (assoc vector 0 ""))

(defn- make-from-vector
  [vector]
  (apply make (take 4 vector)))


;------------------ DIN5008 Handling - Local
(def din5008-local-regex #"^(0[1-9]\d{1,3})\s([1-9]\d{2,})(?:-(\d+))?$")

(defn to-din5008-local
  "Converts to DIN5009 format '0AAAA BBBBBB'"
  [phone]
  (str
    (prepend-zero (:prefix phone))
    " "
    (:number phone)
    (when (not (= "" (:extension phone)))
      (str "-" (:extension phone)))))


(defn is-din5008-local
  "Determines whether a raw number is in DIN5008 local format"
  [raw]
  (if (re-matches din5008-local-regex raw)
    true
    false))

(defn from-din5008-local
  "Parses a phone number from a DIN5008 local formatted string"
  [raw]
  (when (is-din5008-local raw)
    (make-from-vector
      (whitespace-first-item
        (re-matches din5008-local-regex raw)))))


;------------------ DIN5008 Handling - International
(def din5008-international-regex #"^(\+[1-9]{2})\s([1-9]\d{1,3})\s([1-9]\d{2,})$")

(defn to-din5008-international
  "Converts to international DIN5009 format '+49 AAAA BBBBBB'"
  [phone]
  (str
    (:country phone)
    " "
    (remove-leading-zero (:prefix phone))
    " "
    (:number phone)))

(defn is-din5008-international
  "Determines whether a raw number is in DIN5008 local format"
  [raw]
  (if (re-matches din5008-international-regex raw)
    true
    false))

(defn from-din5008-international
  "Parses a phone number from a DIN5008 international formatted string"
  [raw]
  (when (is-din5008-international raw)
    (make-from-vector (drop 1 (re-matches din5008-international-regex raw)))))


;------------------ E.123 Handling - Local
(def e123-local-regex #"^\((0[1-9]{2,4})\)\s([1-9]\d{2,})$")

(defn to-e123-local
  "Converts to E.123 format '(0AAAA) BBBBBB'"
  [phone]
  (str
    "("
    (prepend-zero (:prefix phone))
    ") "
    (:number phone)))

(defn is-e123-local
  "Determines whether a raw number is in E.123 local format"
  [raw]
  (if (re-matches e123-local-regex raw)
    true
    false))

(defn from-e123-local
  "Parses a phone number from a E.123 local formatted string"
  [raw]
  (when (is-e123-local raw)
    (make-from-vector (whitespace-first-item (re-matches e123-local-regex raw)))))


;------------------ E.123 Handling - International
(def e123-international-regex #"^(\+[1-9]{2})\s\(([1-9]{2,4})\)\s([1-9]\d{2,})$")

(defn to-e123-international
  "Converts to international E.123 format '+49 AAAA BBBBBB'"
  [phone]
  (to-din5008-international phone))

(defn is-e123-international
  "Determines whether a raw number is in E.123 international format"
  [raw]
  (if (re-matches e123-international-regex raw)
    true
    false))

(defn from-e123-international
  "Parses a phone number from a E.123 international formatted string"
  [raw]
  (when (is-e123-international raw)
    (make-from-vector (drop 1 (re-matches e123-international-regex raw)))))


;------------------ Microsoft format handling
(defn to-microsoft
  "Converts to international E.123 format '+49 (AAAA) BBBBBB'"
  [phone]
  (str
    (:country phone)
    " ("
    (remove-leading-zero (:prefix phone))
    ") "
    (:number phone)))

(defn is-microsoft
  "Determines whether a raw number is in microsoft format"
  [raw]
  (is-e123-international raw))

(defn from-microsoft
  "Parses a phone number from a microsoft formatted string"
  [raw]
  (from-e123-international raw))


;------------------ Parsing
(defn parse
  "Parses a phone number automatically by guessing the format"
  [raw]
  (cond
    (is-din5008-local raw) (from-din5008-local raw)
    (is-din5008-international raw) (from-din5008-international raw)
    (is-e123-local raw) (from-e123-local raw)
    (is-e123-international raw) (from-e123-international raw)
    :else nil))