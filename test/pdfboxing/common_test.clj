(ns pdfboxing.common-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.common :refer :all])
  (:import [org.apache.pdfbox.pdmodel PDDocument]))


(deftest pdf-existance-check
  (testing "If document supplied is a PDF"
    (is (false? (is-pdf? "test/pdfs/a.txt")))
    (is (true? (is-pdf? "test/pdfs/hello.pdf")))))

(deftest obtain-document-returns-pddocument-if-provided-string-check
  (testing "obtain-document returns a PDDocument if a string path to a PDF is a supplied"
    (let [doc (obtain-document "test/pdfs/hello.pdf")]
      (try
        (is (true? (instance? PDDocument doc)))
        (finally
          (when (instance? PDDocument doc)
            (.close doc)))))))

(deftest obtain-document-returns-pddocument-if-provided-pddocument-check
  (testing "obtain-document returns a PDDocument if PDDocument is provided"
    (let [doc1 (obtain-document "test/pdfs/hello.pdf")
          doc2 (obtain-document doc1)]
      (try
        (is (true? (instance? PDDocument doc2)))
        (finally
          (when (instance? PDDocument doc2)
            (.close doc2)))))))

(deftest obtain-document-returns-pddocument-if-provided-file-check
  (testing "obtain-document returns a PDDocument if a string path to a PDF is a supplied"
    (let [doc (obtain-document (io/file "test/pdfs/hello.pdf"))]
      (try
        (is (true? (instance? PDDocument doc)))
        (finally
          (when (instance? PDDocument doc)
            (.close doc)))))))
