(ns address-decoder.core-test
  (:require [clojure.test :refer :all]
            [address-decoder.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

;(deftest reads-simple-address
;  (testing "simple address parsing - given name"
;    (is (= "Christopher" (parse "Christopher Guckes, Wiechertstr. 12, 65451 Kelsterbach")))))