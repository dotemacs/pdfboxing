(ns pdfboxing.split-test
  (:require [clojure.test :refer [deftest is]]
            [pdfboxing.split :as split]))

(deftest pdf-input-arg-check
  "Test to ensure that the invalid input throws meaningful exception"
  (is (thrown? IllegalArgumentException #"input must be a string" (split/split-pdf :input 12 :start 1 :end 2)))
  (is (thrown? IllegalArgumentException #"input must be a string" (split/split-pdf :input 12)))
  (is (thrown? IllegalArgumentException #"input must be a string" (split/split-pdf :input 12 :start 1)))
  (is (thrown? IllegalArgumentException #"input must be a string" (split/split-pdf :input 12 :end 2)))
  (is (thrown? IllegalArgumentException #"input must be a string" (split/split-pdf :input 12 :start 0)))
  (is (thrown? IllegalArgumentException #"input must be a string" (split/split-pdf :start 1 :end 2)))
  (is (thrown? IllegalArgumentException #"input must be a string" (split/split-pdf :input []))))

(deftest pdf-start-arg-check
  "Test to ensure that :start and :end are valid integers"
  (is (thrown? IllegalArgumentException #":start and :end may only be integers" (split/split-pdf :input "test/pdfs/multi-page.pdf" :start :end)))
  (is (thrown? IllegalArgumentException #":start and :end may only be integers" (split/split-pdf :input "test/pdfs/multi-page.pdf" :end "foo")))
  (is (thrown? IllegalArgumentException #":start and :end may only be integers" (split/split-pdf :input "test/pdfs/multi-page.pdf" :start "bar")))
  (is (thrown? IllegalArgumentException #":start and :end may only be integers" (split/split-pdf :input "test/pdfs/multi-page.pdf" :end "foo" :start "bar"))))


(deftest pdf-file-split
  "Test that the multi-page PDF was turned into multiple PDDocuments"
  (let [file "tests/pdfs/multi-page.pdf"
        splitting-outcome (split/split-pdf :input "test/pdfs/multi-page.pdf")]
    (is (true? (> (count splitting-outcome) 0)))
    (doall (for [doc splitting-outcome]
             (try
               (is (instance? org.apache.pdfbox.pdmodel.PDDocument doc))
               (finally
                 (when (instance? org.apache.pdfbox.pdmodel.PDDocument doc)
                   (.close doc))))))))
