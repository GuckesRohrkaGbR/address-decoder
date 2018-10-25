(ns address-decoder.types.contact-test
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.contact :as contact]))

(deftest contact-creation-test
  (testing "empty contact creation"
    (let [{:keys [cont-name
                  address
                  phone]}
          (contact/make)]

      (is (= nil cont-name))
      (is (= nil address))
      (is (= nil phone))))

  (testing "filled contact creation"
    (is (= "1" (:contact-name (contact/make "1" nil nil))))
    (is (= "2" (:address (contact/make nil "2" nil))))
    (is (= "3" (:phone (contact/make nil nil "3"))))))

(deftest contact-parsing-test
  (testing "splitting-name-and-address"
    (let [simple-address "Max Muster\nMusterstr. 1\n65432 Musterstadt"]
      (is (=
            ["Max Muster" "Musterstr. 1\n65432 Musterstadt"]
            (#'contact/split-name-and-address simple-address))))))


