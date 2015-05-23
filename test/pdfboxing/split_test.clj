(ns pdfboxing.split-test
  (:require [pdfboxing.split :refer :all]
            [clojure.test :refer :all]
            [pdfboxing.common :as common]
            [pdfboxing.split :as split]))

(deftest pdf-file-split
  "Test that the multi-page PDF was turned into multiple PDDocuments"
  (let [file "tests/pdfs/multi-page.pdf"
        splitting-outcome (split/split-pdf {:input "test/pdfs/multi-page.pdf"})]
    (is (true? (> (count splitting-outcome) 0)))))
