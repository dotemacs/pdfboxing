(ns pdfboxing.form-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.form :refer :all]))

(deftest document-fields-and-value
  (def document-fields-with-values {"HIGH SCHOOL DIPLOMA" "",
                                    "TRADE CERTIFICATE" "",
                                    "COLLEGE NO DEGREE" "",
                                    "PHD" "",
                                    "OTHER DOCTORATE" "",
                                    "ASSOCIATES DEGREE" "",
                                    "MASTERS DEGREE" "",
                                    "PROFESSIONAL DEGREE" "",
                                    "STATE" "",
                                    "ZIP" "",
                                    "Name_Last" "",
                                    "Name_First" "",
                                    "Name_Middle" "",
                                    "Name_Suffix" "",
                                    "Name_Prefix" "",
                                    "Telephone_Home" "",
                                    "Telephone_Work" "",
                                    "SSN" "",
                                    "BACHELORS DEGREE" "",
                                    "Address_1" "",
                                    "Address_2" "",
                                    "City" "",
                                    "Emergency_Phone" "",
                                    "Emergency_Contact" "",
                                    "Emergency_Relationship" "",
                                    "Sex" "",
                                    "Birthdate" "",
                                    "Print" ""})
  (is (= document-fields-with-values (get-fields "pdfs/interactiveform.pdf"))))

(deftest populating-fields
  (defn clean-up
    "clean up the file after testing"
    [file]
    (if (.exists (io/as-file file))
      (io/delete-file file)))


  (testing "Error handling for non simple fonts"
    (is (= "Error: can't add non-simple fonts, this is a constraint of PDFBox."
           (set-fields "pdfs/interactiveform.pdf"
                       "pdfs/filled-in.pdf"
                       {"Name_Last" "Last",
                        "Name_First" "First",
                        "Name_Middle" "Middle"}))))

  (testing "Non existent field filling"
    (is (= "Error: non existent field provided"
           (set-fields "pdfs/fillable.pdf" "pdfs/test.pdf" {"non-existent" "fail"}))))

  (testing "form filling valid fields"
    (is (nil? (set-fields "pdfs/fillable.pdf" "pdfs/test.pdf" {"Text10" "My first name"}))))
  (clean-up "pdfs/test.pdf"))
