(ns address-decoder.types.contact-test
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.contact :as contact]))

(deftest contact-test
  (testing "empty contact creation"
    (is (= "" (:given-name (contact/make))))
    (is (= "" (:surname (contact/make))))
    (is (= "" (:company-name (contact/make))))
    (is (= nil (:address (contact/make))))
    (is (= nil (:phone (contact/make)))))


  (testing "person contact creation"
    (is (= "Christopher" (:given-name (contact/make "Christopher" "Thonfeld-Guckes" nil nil))))
    (is (= "Thonfeld-Guckes" (:surname (contact/make "Christopher" "Thonfeld-Guckes" nil nil))))
    (is (= nil (:address (contact/make "Christopher" "Thonfeld-Guckes" nil nil))))
    (is (= nil (:phone (contact/make "Christopher" "Thonfeld-Guckes" nil nil)))))


  (testing "company contact creation"
    (is (= "Guckes, Rohrka GbR" (:company-name (contact/make "Guckes, Rohrka GbR" nil nil))))
    (is (= nil (:address (contact/make "Guckes, Rohrka GbR" nil nil))))
    (is (= nil (:phone (contact/make "Guckes, Rohrka GbR" nil nil))))))


