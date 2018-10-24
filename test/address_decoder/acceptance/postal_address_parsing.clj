(ns address-decoder.acceptance.postal-address-parsing
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.contact :as contact]))

(def simple-person-address
  "Max Muster
   Musterstr. 1
   65432 Musterstadt")

(def simple-company-address
  "Guckes Rohrka GbR
   Musterstr. 1
   65432 Musterstadt")

(deftest postal-address-parsing-test
  (testing "parsing simple personal address"
    (let [contact (contact/parse simple-person-address)]
      (let [contact-name (:contact-name contact)]
        (is (not (= nil contact-name)))
        (is (= "Max" (:given-names contact-name)))
        (is (= "Muster" (:surnames contact-name))))
      (let [addr (:address contact)]
        (is (not (= nil addr)))
        (is (= "Musterstr." (:street addr)))
        (is (= "1" (:number addr)))
        (is (= "65432" (:zip addr)))
        (is (= "Musterstadt" (:city addr))))))

  (testing "parsing simple organization address"
    (let [contact (contact/parse simple-company-address)]
      (let [contact-name (:contact-name contact)]
        (is (not (= nil contact-name)))
        (is (= "Guckes Rohrka GbR" (:organization-name contact-name))))
      (let [addr (:address contact)]
        (is (not (= nil addr)))
        (is (= "Musterstr." (:street addr)))
        (is (= "1" (:number addr)))
        (is (= "65432" (:zip addr)))
        (is (= "Musterstadt" (:city addr)))))))