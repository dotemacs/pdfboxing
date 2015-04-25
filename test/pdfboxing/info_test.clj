(ns pdfboxing.info-test
  (:require [clojure.test :refer :all]
            [pdfboxing.info :refer :all]))

(deftest document-info
  (testing "page count"
    (is (= 1 (page-number "test/pdfs/interactiveform.pdf"))))
  (testing "field count"
    (is (= 10 (count (about-doc "test/pdfs/interactiveform.pdf")))))
  (testing "for the key names"
    (is (= #{"title" "author" "subject" "keywords" "creator" "producer" "trapped" "metadata-keys" "creation-date" "modification-date"}
           (set (keys (about-doc "test/pdfs/interactiveform.pdf"))))))
  (testing "if the full document info matches"
    (is (= {"title" "Example of an Interactive PDF Form",
            "author" nil,
            "subject" "PDF forms",
            "keywords" "forms, flat, fillable",
            "trapped" nil,
            "metadata-keys" #{"AAPL:Keywords" "CreationDate" "Creator" "Keywords" "ModDate" "Producer" "Subject" "Title"}
            "creator" "Adobe InDesign CS3 (5.0.4)",
            "producer" "Mac OS X 10.8.5 Quartz PDFContext"}
           (dissoc (about-doc "test/pdfs/interactiveform.pdf")
                   "creation-date" ;#inst "2013-12-14T13:44:39.000+00:00"
                   "modification-date" ;#inst "2013-12-14T13:44:39.000+00:00"
                   ))))
  (testing "for the metadata value"
    (is (= "Example of an Interactive PDF Form"
           (metadata-value "test/pdfs/interactiveform.pdf" "Title")))
    (is (= "D:20131214134439Z00'00'"
           (metadata-value "test/pdfs/interactiveform.pdf" "CreationDate")))
    ))
