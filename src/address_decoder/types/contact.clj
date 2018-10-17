(ns address-decoder.types.contact)

(defrecord contact [given-name surname company-name address phone])

(defn make
  ([]
   (contact. "" "" "" nil nil))

  ([given-name surname address phone]
   (contact. given-name surname "" address phone))

  ([company-name address phone]
   (contact. "" "" company-name address phone))
  )