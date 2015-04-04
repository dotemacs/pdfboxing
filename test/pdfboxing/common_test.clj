(ns pdfboxing.common-test
  (:require [clojure.test :refer :all]
            [pdfboxing.common :refer :all]))

(deftest pdf-existance-check
  (testing "If document supplied is a PDF"
    (is (false? (is-pdf? "test/pdfs/a.txt")))
    (is (true? (is-pdf? "test/pdfs/hello.pdf")))))
