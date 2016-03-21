(ns pdfboxing.info-test
  (:require [clojure.test :refer :all]
            [pdfboxing.info :refer :all]))

(deftest document-info
  (testing "page count"
    (is (= 1 (page-number "test/pdfs/interactiveform.pdf"))))
  (testing "field count"
    (is (= 10 (count (about-doc "test/pdfs/interactiveform.pdf")))))
  (testing "for the key names"
    (is (= #{"title" "author" "subject" "keywords" "creator" "producer"
             "trapped" "metadata-keys" "creation-date"
             "modification-date"}
           (set (keys (about-doc "test/pdfs/interactiveform.pdf"))))))
  (testing "if the full document info matches"
    (is (= {"author" "Bruce",
            "creator" "Microsoft Word - Fillable_PDF_Sample_from_TheWebJockeys_vB.doc",
            "producer" "ScanSoft PDF Create! 4",
            "subject" nil,
            "keywords" nil,
            "title" "Microsoft Word - Fillable_PDF_Sample_from_TheWebJockeys_vB.doc",
            "metadata-keys" #{"Author" "CreationDate" "Creator" "ModDate" "Producer" "Title"}, "trapped" nil}
           (dissoc (about-doc "test/pdfs/interactiveform.pdf")
                   "creation-date"
                   "modification-date"))))
  (testing "only requested keys value matches"
    (is (= {"title" "Microsoft Word - Fillable_PDF_Sample_from_TheWebJockeys_vB.doc", "subject" nil}
           (about-doc "test/pdfs/interactiveform.pdf"
                      :keys ["title" "subject"]))))
  (testing "for the metadata value"
    (is (= "Microsoft Word - Fillable_PDF_Sample_from_TheWebJockeys_vB.doc"
           (metadata-value "test/pdfs/interactiveform.pdf" "Title")))
    (is (= "D:20070222150352-05'00'"
           (metadata-value "test/pdfs/interactiveform.pdf"
                           "CreationDate")))
    (is (= {"Author" "Bruce",
            "CreationDate" "D:20070222150352-05'00'",
            "Creator" "Microsoft Word - Fillable_PDF_Sample_from_TheWebJockeys_vB.doc",
            "ModDate" "D:20070222222331-05'00'",
            "Producer" "ScanSoft PDF Create! 4",
            "Title" "Microsoft Word - Fillable_PDF_Sample_from_TheWebJockeys_vB.doc"}
           (metadata-values "test/pdfs/interactiveform.pdf")))))
