(ns address-decoder.types.phone-test
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.phone :as phone]))

(def international (phone/make "+49" "6123" "1234567"))
(def wrong-zero (phone/make "+49" "06123" "1234567"))
(def din-local (phone/make "06123" "1234567"))
(def din-local-ext (phone/make "" "06123" "1234567" "89"))

(def din-local-string "06123 1234567")
(def din-local-ext-string "06123 1234567-89")
(def din-int-string "+49 6123 1234567")
(def e123-local-string "(06123) 1234567")
(def microsoft-string "+49 (6123) 1234567")
(def braced-wrong-zero "+49 (06123) 1234567")
(def int-leading-zero-string "+49 06123 1234567")
(def int-without-plus "49 (69) 123")
(def local-without-zero "(6123) 1234567")
(def e123-int-string "+49 (69) 123")

(deftest phone-test
  (testing "empty phone number creation"
    (let [{:keys [country
                  prefix
                  number]}
          (phone/make)]
      (is (= "" country))
      (is (= "" prefix))
      (is (= "" number))))

  (testing "local phone number creation"
    (let [{:keys [
                  prefix
                  number]}
          din-local]
      (is (= "06123" prefix))
      (is (= "1234567" number))))

  (testing "global phone number creation"
    (let [{:keys [country
                  prefix
                  number]} international]
      (is (= "+49" country))
      (is (= "06123" prefix))
      (is (= "1234567" number))))

  (testing "DIN5008 conversion"
    (is (= din-local-string (phone/to-din5008-local international)))
    (is (= din-local-string (phone/to-din5008-local wrong-zero)))
    (is (= din-local-string (phone/to-din5008-local din-local)))
    (is (= din-local-ext-string (phone/to-din5008-local din-local-ext)))

    (is (= din-int-string (phone/to-din5008-international international)))
    (is (= din-int-string (phone/to-din5008-international wrong-zero))))

  (testing "E.123 conversion"
    (is (= e123-local-string (phone/to-e123-local international)))
    (is (= e123-local-string (phone/to-e123-local wrong-zero)))
    (is (= e123-local-string (phone/to-e123-local din-local)))

    (is (= din-int-string (phone/to-e123-international international)))
    (is (= din-int-string (phone/to-e123-international wrong-zero))))

  (testing "Microsoft conversion"
    (is (= microsoft-string (phone/to-microsoft international)))
    (is (= microsoft-string (phone/to-microsoft wrong-zero))))

  (testing "Raw phone number identification"
    (is (= true (phone/is-din5008-local din-local-string)))
    (is (= true (phone/is-din5008-local din-local-ext-string)))
    (is (= false (phone/is-din5008-local microsoft-string)))
    (is (= false (phone/is-din5008-local braced-wrong-zero)))
    (is (= false (phone/is-din5008-local din-int-string)))
    (is (= false (phone/is-din5008-local int-leading-zero-string)))
    (is (= false (phone/is-din5008-local int-without-plus)))
    (is (= false (phone/is-din5008-local e123-local-string)))
    (is (= false (phone/is-din5008-local local-without-zero)))

    (is (= true (phone/is-din5008-international din-int-string)))
    (is (= false (phone/is-din5008-international din-local-ext-string)))
    (is (= false (phone/is-din5008-international microsoft-string)))
    (is (= false (phone/is-din5008-international braced-wrong-zero)))
    (is (= false (phone/is-din5008-international din-local-string)))
    (is (= false (phone/is-din5008-international int-leading-zero-string)))
    (is (= false (phone/is-din5008-international int-without-plus)))
    (is (= false (phone/is-din5008-international e123-local-string)))
    (is (= false (phone/is-din5008-international local-without-zero)))

    (is (= true (phone/is-e123-local e123-local-string)))
    (is (= false (phone/is-e123-local din-local-ext-string)))
    (is (= false (phone/is-e123-local microsoft-string)))
    (is (= false (phone/is-e123-local e123-int-string)))
    (is (= false (phone/is-e123-local braced-wrong-zero)))
    (is (= false (phone/is-e123-local din-int-string)))
    (is (= false (phone/is-e123-local int-leading-zero-string)))
    (is (= false (phone/is-e123-local int-without-plus)))
    (is (= false (phone/is-e123-local din-local-string)))
    (is (= false (phone/is-e123-local local-without-zero)))

    (is (= true (phone/is-e123-international microsoft-string)))
    (is (= true (phone/is-e123-international e123-int-string)))
    (is (= false (phone/is-e123-international din-local-ext-string)))
    (is (= false (phone/is-e123-international braced-wrong-zero)))
    (is (= false (phone/is-e123-international din-int-string)))
    (is (= false (phone/is-e123-international int-leading-zero-string)))
    (is (= false (phone/is-e123-international int-without-plus)))
    (is (= false (phone/is-e123-international din-local-string)))
    (is (= false (phone/is-e123-international e123-local-string)))
    (is (= false (phone/is-e123-international local-without-zero)))

    (is (= true (phone/is-microsoft microsoft-string)))
    (is (= true (phone/is-microsoft e123-int-string)))
    (is (= false (phone/is-microsoft braced-wrong-zero)))
    (is (= false (phone/is-microsoft din-local-ext-string)))
    (is (= false (phone/is-microsoft din-int-string)))
    (is (= false (phone/is-microsoft int-leading-zero-string)))
    (is (= false (phone/is-microsoft int-without-plus)))
    (is (= false (phone/is-microsoft din-local-string)))
    (is (= false (phone/is-microsoft e123-local-string)))
    (is (= false (phone/is-microsoft local-without-zero))))

  (testing "Parsing"
    (let [{:keys [country
                  prefix
                  number]}
          (phone/from-din5008-local din-local-string)]
      (is (= "" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [country
                  prefix
                  number
                  extension]}
          (phone/from-din5008-local din-local-ext-string)]
      (is (= "" country))
      (is (= "06123" prefix))
      (is (= "1234567" number))
      (is (= "89" extension)))

    (let [{:keys [country
                  prefix
                  number]}
          (phone/from-din5008-international din-int-string)]
      (is (= "+49" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [prefix
                  number]}
          (phone/from-e123-local e123-local-string)]
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [country
                  prefix
                  number]}
          (phone/from-e123-international microsoft-string)]
      (is (= "+49" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [country
                  prefix
                  number]}
          (phone/from-microsoft microsoft-string)]
      (is (= "+49" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [country
                  prefix
                  number]}
          (phone/parse din-local-string)]
      (is (= "" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [country
                  prefix
                  number]}
          (phone/parse din-int-string)]
      (is (= "+49" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [country
                  prefix
                  number]}
          (phone/parse e123-local-string)]
      (is (= "" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (let [{:keys [country
                  prefix
                  number]}
          (phone/parse microsoft-string)]
      (is (= "+49" country))
      (is (= "06123" prefix))
      (is (= "1234567" number)))

    (is (= nil (phone/parse "not-valid")))))

