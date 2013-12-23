(ns pdfboxing.info-test
  (:require [clojure.test :refer :all]
            [pdfboxing.info :refer :all]))

(deftest document-info
  (testing "page count"
    (is (= 1 (page-number "test/pdfs/interactiveform.pdf"))))
  (testing "field count"
    (is (= 6 (count (about-doc "test/pdfs/interactiveform.pdf")))))
  (testing "for the key names"
    (is (= ["title" "author" "subject" "keywords" "creator" "producer"]
           (keys (about-doc "test/pdfs/interactiveform.pdf")))))
  (testing "if the full document info matches"
    (is (= {"title" "Example of an Interactive PDF Form",
            "author" nil,
            "subject" "PDF forms",
            "keywords" "forms, flat, fillable",
            "creator" "Adobe InDesign CS3 (5.0.4)",
            "producer" "Mac OS X 10.8.5 Quartz PDFContext"}
           (about-doc "test/pdfs/interactiveform.pdf")))))
