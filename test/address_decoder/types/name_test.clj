(ns address-decoder.types.name-test
  (:require [clojure.test :refer :all])
  (:require [address-decoder.types.contact-name :as contact-name]))

(deftest contact-name-creation-test
  (testing "empty name creation"
    (let [{:keys [given-names
                  surnames
                  organization-name]}
          (contact-name/make)]

      (is (= "" given-names))
      (is (= "" surnames))
      (is (= "" organization-name))))

  (testing "person name creation"
    (is (= "Max" (:given-names (contact-name/make "Max" "Muster"))))
    (is (= "Max Fritz" (:given-names (contact-name/make "Max Fritz" "Muster"))))
    (is (= "Muster" (:surnames (contact-name/make "Max" "Muster")))))

  (testing "company name creation"
    (is (= "Guckes Rohrka GbR" (:organization-name (contact-name/make "Guckes Rohrka GbR")))))

  (testing "contact person name creation"
    (let [{:keys [given-names
                  surnames
                  organization-name]}
          (contact-name/make "Guckes Rohrka GbR" "Max" "Muster")]

      (is (= "Max" given-names))
      (is (= "Muster" surnames))
      (is (= "Guckes Rohrka GbR" organization-name)))))

(deftest contact-name-parsing
  (testing "company name parsing"
    (letfn [(is-organization
              [org-name]
              (is (= org-name (:organization-name (contact-name/parse org-name)))))]
      (is-organization "Guckes Rohrka GbR")
      (is-organization "Guckes Rohrka GmbH")
      (is-organization "Guckes Rohrka OHG")
      (is-organization "Guckes Rohrka KG")
      (is-organization "Guckes Rohrka KGaA")
      (is-organization "Guckes Rohrka eG")
      (is-organization "Guckes Rohrka e.V.")
      (is-organization "Guckes Rohrka UG")
      (is-organization "Guckes Rohrka VVaG")))

  (testing "person name parsing"
    (is (= "Max" (:given-names (contact-name/parse "Max Mustermann"))))
    (is (= "Max Wilhelm" (:given-names (contact-name/parse "Max Wilhelm Mustermann"))))
    (is (= "Mustermann" (:surnames (contact-name/parse "Max Mustermann"))))
    (is (= "Mustermann-Meyer" (:surnames (contact-name/parse "Max Mustermann-Meyer"))))))
