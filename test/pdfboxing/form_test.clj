(ns pdfboxing.form-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest is testing]]
            [pdfboxing.form :refer [get-fields rename-fields set-fields]]
            [pdfboxing.common :as common]))

(defn clean-up
  "clean up the file after testing"
  [file]
  (if (.exists (io/as-file file))
    (io/delete-file file)))

(deftest document-fields-and-value
  (def document-fields-with-values {"last_name" ""
                                    "first_name" ""
                                    "date" ""
                                    "checkbox1" "Off"
                                    "checkbox2" "Off"
                                    "checkbox3" "Off"
                                    "checkbox4" "Off"
                                    "checkbox5" "Off"})
  (is (= document-fields-with-values (get-fields "test/pdfs/interactiveform.pdf"))))


(deftest nested-fields
  (testing "Check that we can fill out nested forms"
    (set-fields "test/pdfs/claim.pdf"
                "test/pdfs/filled-in.pdf"
                [["25" 0 "Amount"]])
    (is (= "25" (->>
                 "test/pdfs/filled-in.pdf"
                 common/obtain-document
                 common/get-form
                 .getFields
                 (filter #(= (.getPartialName %1) "Amount"))
                 first
                 .getChildren
                 first
                 .getValue)))
    (clean-up "test/pdfs/filled-in.pdf")))

(deftest populating-fields
  (testing "Error handling for non simple fonts"
    (is (thrown? java.io.IOException
                 (set-fields "test/pdfs/old-interactiveform.pdf"
                             "test/pdfs/filled-in.pdf"
                             {"Name_Last" "Last"
                              "Name_First" "First"
                              "Name_Middle" "Middle"}))))

  (testing "Non existent field filling"
    (is (thrown? IllegalArgumentException
                 (set-fields "test/pdfs/fillable.pdf" "test/pdfs/test.pdf" {"non-existent" "fail"}))))

  (testing "form filling valid fields"
    (is (nil? (set-fields "test/pdfs/interactiveform.pdf" "test/pdfs/test.pdf" {"first_name" "My first name"}))))
  (clean-up "test/pdfs/test.pdf"))

(deftest field-rename
  (testing "renaming of the form field"
    (is (true?
         (contains?
          (do
            (rename-fields "test/pdfs/interactiveform.pdf" "test/pdfs/addr1.pdf" {"last_name" "new_last_name"})
            (get-fields "test/pdfs/addr1.pdf"))
          "new_last_name"))))
  (clean-up "test/pdfs/addr1.pdf"))
