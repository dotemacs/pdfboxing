(ns pdfboxing.util-test
  (:require [clojure.test :refer :all]
            [pdfboxing.util :refer :all]))

(deftest first-line-validation
  (testing "the contents of the first line"
    (is (false? (first-line-valid? "%PDF-2")))
    (is (true? (first-line-valid? "%PDF-1.9")))))
