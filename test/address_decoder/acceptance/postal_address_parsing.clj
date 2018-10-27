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

(def person-address-with-contact
  "Max Muster
   Musterstr. 1
   65432 Musterstadt
   Tel.: (06123) 987654
   Fax.: +49 (6123) 987653
   E-Mail: max@muster.de")

(def company-address-with-contact
  "Guckes Rohrka GbR
   Musterstr. 1
   65432 Musterstadt
   Tel.: (06123) 987654
   Fax.: +49 (6123) 987653
   E-Mail: info@muster.de
   http://www.torq-dev.de")

(def mail-signature-no-1
  "Christopher Guckes
   Geschäftsführer

   E-Mail: christopher.guckes@torq-dev.de
   Telefon: +49-(0)6107-6929790

   --

   Guckes, Rohrka GbR
   Wiechertstr. 12
   65451 Kelsterbach
   Deutschland

   Telefon: +49 (6107) 6929790
   Fax: +49 (6107) 6929791
   E-Mail: info@torq-dev.de
   www: www.torq-dev.de
   Geschäftsführung: Christopher Guckes, Fabian Rohrka")

(def mail-signature-no-2
  "Max Muster
   Muster GmbH
   --------------------------------------------
   eMail: mm@muster.de
   URL: http://www.example.de/
   Tel.: +49 6543 12345-11
   Fax: +49 6543 12345-12
   Muster GmbH
   Musterstr. 136a-c
   65432 Musterstadt
   Germany
   --------------------------------------------
   Muster GmbH, Musterstadt
   Amtsgericht Musterstad, HRB 12345
   Geschäftsführer: Manfred Muster, Maximilian Muster, Mauritz Muster")

(deftest postal-address-parsing-test
  (testing "parsing simple personal address"
    (let [contact (contact/parse simple-person-address)
          contact-name (:contact-name contact)
          addr (:address contact)]

      (is (not (= nil contact-name)))
      (is (not (= nil addr)))

      (is (= "Max" (:given-names contact-name)))
      (is (= "Muster" (:surnames contact-name)))
      (is (= "Musterstr." (:street addr)))
      (is (= "1" (:number addr)))
      (is (= "65432" (:zip addr)))
      (is (= "Musterstadt" (:city addr)))))

  (testing "parsing simple organization address"
    (let [contact (contact/parse simple-company-address)
          contact-name (:contact-name contact)
          addr (:address contact)]

      (is (not (= nil contact-name)))
      (is (not (= nil addr)))

      (is (= "Guckes Rohrka GbR" (:organization-name contact-name)))
      (is (= "Musterstr." (:street addr)))
      (is (= "1" (:number addr)))
      (is (= "65432" (:zip addr)))
      (is (= "Musterstadt" (:city addr))))))
