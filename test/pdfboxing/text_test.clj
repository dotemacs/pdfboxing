(ns pdfboxing.text-test
  (:require [clojure.test :refer :all]
            [pdfboxing.text :refer :all]))

(deftest text-extraction
  (is (= "Hello, this is pdfboxing.text\n"
         (extract "test/pdfs/hello.pdf"))))
