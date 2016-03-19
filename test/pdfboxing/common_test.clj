(ns pdfboxing.common-test
  (:require [clojure.test :refer :all]
            [pdfboxing.common :refer :all]))

(deftest pdf-existance-check
  (testing "If document supplied is a PDF"
    (is (false? (is-pdf? "test/pdfs/a.txt")))
    (is (true? (is-pdf? "test/pdfs/hello.pdf")))))

(deftest obtain-document-returns-pddocument-if-provided-string-check
  (testing "obtain-document returns a PDDocument if a string path to a PDF is a supplied"
    (is (true?  (instance?
                   org.apache.pdfbox.pdmodel.PDDocument
                   (obtain-document "test/pdfs/hello.pdf"))))))

(deftest obtain-document-returns-pddocument-if-provided-pddocument-check
  (testing "obtain-document returns a PDDocument if PDDocument is provided"
    (is (true?  (instance?
                   org.apache.pdfbox.pdmodel.PDDocument
                   (obtain-document (obtain-document "test/pdfs/hello.pdf")))))))
