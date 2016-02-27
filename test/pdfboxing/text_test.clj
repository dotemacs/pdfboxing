(ns pdfboxing.text-test
  (:require [clojure.test :refer :all]
            [pdfboxing.text :refer :all]
            [pdfboxing.split :refer :all]))

(deftest text-extraction-from-pddoc
  (is (= "Hello, this is pdfboxing.text\n"
         (extract-from-pddocument 
           ((split-pdf :input "test/pdfs/hello.pdf") 0)))))

(deftest text-extraction
  (is (= "Hello, this is pdfboxing.text\n"
         (extract "test/pdfs/hello.pdf"))))
