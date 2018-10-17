(ns address-decoder.types.phone)

(defrecord phone [country prefix number])

(defn make
  ([]
   (phone. "" "" ""))

  ([prefix number]
   (phone. "" prefix number))

  ([country prefix number]
   (phone. country prefix number)))

(defn- prepend-zero [prefix]
  (if (.startsWith prefix "0")
    prefix
    (str "0" prefix)))

(defn- remove-leading-zero [prefix]
  (if (.startsWith prefix "0")
    (.substring prefix 1)
    prefix))

(defn din5008-local
  "Converts to DIN5009 format '0AAAA BBBBBB'"
  [phone]
  (str
    (prepend-zero (:prefix phone))
    " "
    (:number phone)))

(defn din5008-international
  "Converts to international DIN5009 format '+49 AAAA BBBBBB'"
  [phone]
  (str
    (:country phone)
    " "
    (remove-leading-zero (:prefix phone))
    " "
    (:number phone)))

(defn e123-local
  "Converts to E.123 format '(0AAAA) BBBBBB'"
  [phone]
  (str
    "("
    (prepend-zero (:prefix phone))
    ") "
    (:number phone)))

(defn e123-international
  "Converts to international E.123 format '+49 AAAA BBBBBB'"
  [phone]
  (din5008-international phone))


(defn microsoft
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
  (if (re-matches #"^\+[1-9]{2}\s\([1-9]{2,4}\)\s[1-9]\d{2,}$" raw)
    :true
    :false))

(defn is-din5008-local
  "Determines whether a raw number is in DIN5008 local format"
  [raw]
  (if (re-matches #"^0[1-9]\d{1,3}\s[1-9]\d{2,}$" raw)
    :true
    :false))

(defn is-din5008-international
  "Determines whether a raw number is in DIN5008 local format"
  [raw]
  (if (re-matches #"^\+[1-9]{2}\s[1-9]\d{1,3}\s[1-9]\d{2,}$" raw)
    :true
    :false))

(defn parse
  "Parses a phone number automatically by guessing the format"
  [raw]
  (make "" "" ""))




