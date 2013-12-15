(ns pdfboxing.form-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.form :refer :all])
  (:import [java.io FileNotFoundException]
           [clojure.lang ArityException]))

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
