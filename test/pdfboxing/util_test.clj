(ns pdfboxing.util-test
  (:require [clojure.test :refer :all]
            [pdfboxing.util :refer :all]))

(deftest first-line-validation
  (testing "the contents of the first line"
    (is (false? (first-line-valid? "%PDF-2")))
    (is (true? (first-line-valid? "%PDF-1.9")))))

(deftest lenght-of-line
  (testing "line length of the second line of a given PDF file"
    (is (false? (line-long-enough? "123456789")))
    (is (true? (line-long-enough? "12345")))))
