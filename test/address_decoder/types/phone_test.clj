(ns address-decoder.types.phone-test
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.phone :as phone]))

(deftest phone-test
  (testing "empty phone number creation"
    (is (= "" (:country (phone/make))))
    (is (= "" (:prefix (phone/make))))
    (is (= "" (:number (phone/make)))))


  (testing "local phone number creation"
    (is (= "06123" (:prefix (phone/make "06123" "1234567"))))
    (is (= "1234567" (:number (phone/make "06123" "1234567")))))


  (testing "global phone number creation"
    (is (= "+49" (:country (phone/make "+49" "6123" "1234567"))))
    (is (= "6123" (:prefix (phone/make "+49" "6123" "1234567"))))
    (is (= "1234567" (:number (phone/make "+49" "6123" "1234567")))))


  (testing "DIN5008 conversion"
    (is (= "06123 1234567" (phone/din5008-local (phone/make "+49" "6123" "1234567"))))
    (is (= "06123 1234567" (phone/din5008-local (phone/make "+49" "06123" "1234567"))))
    (is (= "06123 1234567" (phone/din5008-local (phone/make "06123" "1234567"))))

    (is (= "+49 6123 1234567" (phone/din5008-international (phone/make "+49" "6123" "1234567"))))
    (is (= "+49 6123 1234567" (phone/din5008-international (phone/make "+49" "06123" "1234567")))))

  (testing "E.123 conversion"
    (is (= "(06123) 1234567" (phone/e123-local (phone/make "+49" "6123" "1234567"))))
    (is (= "(06123) 1234567" (phone/e123-local (phone/make "+49" "06123" "1234567"))))
    (is (= "(06123) 1234567" (phone/e123-local (phone/make "06123" "1234567"))))

    (is (= "+49 6123 1234567" (phone/e123-international (phone/make "+49" "6123" "1234567"))))
    (is (= "+49 6123 1234567" (phone/e123-international (phone/make "+49" "06123" "1234567")))))

  (testing "Microsoft conversion"
    (is (= "+49 (6123) 1234567" (phone/microsoft (phone/make "+49" "6123" "1234567"))))
    (is (= "+49 (6123) 1234567" (phone/microsoft (phone/make "+49" "06123" "1234567")))))

  (testing "Raw phone number identification"
    (is (= :true (phone/is-microsoft "+49 (6123) 1234567")))
    (is (= :true (phone/is-microsoft "+49 (69) 123")))
    (is (= :false (phone/is-microsoft "+49 (06123) 1234567")))
    (is (= :false (phone/is-microsoft "+49 6123 1234567")))
    (is (= :false (phone/is-microsoft "+49 06123 1234567")))
    (is (= :false (phone/is-microsoft "49 (69) 123")))
    (is (= :false (phone/is-microsoft "06123 1234567")))
    (is (= :false (phone/is-microsoft "(06123) 1234567")))
    (is (= :false (phone/is-microsoft "(6123) 1234567")))

    (is (= :true (phone/is-din5008-local "06123 1234567")))
    (is (= :false (phone/is-din5008-local "+49 (6123) 1234567")))
    (is (= :false (phone/is-din5008-local "+49 (06123) 1234567")))
    (is (= :false (phone/is-din5008-local "+49 6123 1234567")))
    (is (= :false (phone/is-din5008-local "+49 06123 1234567")))
    (is (= :false (phone/is-din5008-local "49 (69) 123")))
    (is (= :false (phone/is-din5008-local "(06123) 1234567")))
    (is (= :false (phone/is-din5008-local "(6123) 1234567")))

    (is (= :true (phone/is-din5008-international "+49 6123 1234567")))
    (is (= :false (phone/is-din5008-international "+49 (6123) 1234567")))
    (is (= :false (phone/is-din5008-international "+49 (06123) 1234567")))
    (is (= :false (phone/is-din5008-international "06123 1234567")))
    (is (= :false (phone/is-din5008-international "+49 06123 1234567")))
    (is (= :false (phone/is-din5008-international "49 (69) 123")))
    (is (= :false (phone/is-din5008-international "(06123) 1234567")))
    (is (= :false (phone/is-din5008-international "(6123) 1234567"))))
  
  (testing "Parsing"
    (is (= "" (:country (phone/parse "06123 12345"))))
    (is (= "" (:country (phone/parse "(06123) 12345"))))

    (is (= "06123" (:country (phone/parse "06123 12345"))))
    (is (= "06123" (:country (phone/parse "(06123) 12345"))))))


