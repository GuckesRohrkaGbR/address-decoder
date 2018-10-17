(ns address-decoder.types.address-test
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.address :as address]))

(deftest address-test
  (testing "empty address creation"
    (is (= "" (:street (address/make))))
    (is (= "" (:number (address/make))))
    (is (= "" (:co (address/make))))
    (is (= "" (:zip (address/make))))
    (is (= "" (:city (address/make))))
    (is (= "" (:state (address/make))))
    (is (= "" (:country (address/make))))
    (is (= "" (:po-box (address/make))))
    (is (= "" (:po-zip (address/make))))
    (is (= "" (:company-city (address/make))))
    (is (= "" (:company-zip (address/make))))
    )

  (testing "street address creation"
    (is (= "Street" (:street (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "12a-c" (:number (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Chris" (:co (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "12345" (:zip (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Testcity" (:city (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Hessen" (:state (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Deutschland" (:country (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    )

  (testing "po box address creation"
    (is (= "9999" (:po-box (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "12345" (:po-zip (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Testcity" (:po-city (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Hessen" (:state (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Deutschland" (:country (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    )

  (testing "country zip address creation"
    (is (= "12345" (:company-zip (address/make "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Testcity" (:company-city (address/make "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Hessen" (:state (address/make "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Deutschland" (:country (address/make "12345" "Testcity" "Hessen" "Deutschland"))))
    )
  )