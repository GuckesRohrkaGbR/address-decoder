(ns address-decoder.types.address)

(defrecord address [street number co zip city state country po-box po-zip po-city company-zip company-city])

(defn make
  ([]
   (address. "" "" "" "" "" "" "" "" "" "" "" ""))

  ([street number co zip city state country]
   (address. street number co zip city state country "" "" "" "" ""))

  ([po-box po-zip po-city state country]
   (address. "" "" "" "" "" state country po-box po-zip po-city "" ""))

  ([company-zip company-city state country]
   (address. "" "" "" "" "" state country "" "" "" company-zip company-city)))

