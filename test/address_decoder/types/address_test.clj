(ns address-decoder.types.address-test
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.address :as address]))

(deftest address-test-creation
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
    (is (= "" (:company-zip (address/make)))))


  (testing "street address creation"
    (is (= "Street" (:street (address/make "Street" "12a-c" "12345" "Testcity"))))
    (is (= "12a-c" (:number (address/make "Street" "12a-c" "12345" "Testcity"))))
    (is (= "12345" (:zip (address/make "Street" "12a-c" "12345" "Testcity"))))
    (is (= "Testcity" (:city (address/make "Street" "12a-c" "12345" "Testcity"))))

    (is (= "Street" (:street (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "12a-c" (:number (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Chris" (:co (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "12345" (:zip (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Testcity" (:city (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Hessen" (:state (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Deutschland" (:country (address/make "Street" "12a-c" "Chris" "12345" "Testcity" "Hessen" "Deutschland")))))


  (testing "po box address creation"
    (is (= "9999" (:po-box (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "12345" (:po-zip (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Testcity" (:po-city (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Hessen" (:state (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))
    (is (= "Deutschland" (:country (address/make "9999" "12345" "Testcity" "Hessen" "Deutschland"))))))

(deftest address-parsing
  (testing "zip and city parsing"
    (is (= ["65432" "Musterstadt"] (#'address/split-at-horiz-ws "65432 Musterstadt")))
    (is (= ["65432" "Musterstadt"] (#'address/split-at-horiz-ws "65432      Musterstadt")))
    (is (= ["65432" "Musterstadt"] (#'address/split-at-horiz-ws "  65432 Musterstadt  "))))

  (testing "street and number parsing"
    (is (= ["Musterstr." "1a-c"] (#'address/split-at-horiz-ws "Musterstr. 1a-c")))
    (is (= ["Musterstr." "1a-c"] (#'address/split-at-horiz-ws "Musterstr.   1a-c")))
    (is (= ["Musterstr." "1a-c"] (#'address/split-at-horiz-ws "  Musterstr. 1a-c  "))))

  (testing "segment parsing"
    (is (= ["Musterstr. 1a-c" "65432 Musterstadt"] (#'address/split-segments "Musterstr. 1a-c\n65432 Musterstadt")))
    (is (= ["Musterstr. 1a-c" "65432 Musterstadt"] (#'address/split-segments "Musterstr. 1a-c, 65432 Musterstadt")))
    (is (= ["Musterstr. 1a-c" "65432 Musterstadt"] (#'address/split-segments "Musterstr. 1a-c · 65432 Musterstadt"))))

  (testing "tokenizing"
    (is (= ["Musterstr." "1a-c" "65432" "Musterstadt"] (#'address/tokenize "Musterstr. 1a-c\n65432 Musterstadt")))
    (is (= ["Musterstr." "1a-c" "65432" "Musterstadt"] (#'address/tokenize "Musterstr. 1a-c, 65432 Musterstadt")))
    (is (= ["Musterstr." "1a-c" "65432" "Musterstadt"] (#'address/tokenize "Musterstr. 1a-c · 65432 Musterstadt"))))

  (testing "parsing whole addresses"
    (letfn [(address-matches [addr]
              (is (= "Musterstr." (:street addr)))
              (is (= "1a-c" (:number addr)))
              (is (= "65432" (:zip addr)))
              (is (= "Musterstadt" (:city addr))))]
      (let [addr (address/parse "Musterstr. 1a-c\n65432 Musterstadt")]
        (address-matches addr))
      (let [addr (address/parse "Musterstr. 1a-c, 65432 Musterstadt")]
        (address-matches addr))
      (let [addr (address/parse "Musterstr. 1a-c · 65432 Musterstadt")]
        (address-matches addr)))))
