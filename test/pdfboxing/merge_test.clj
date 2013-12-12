(ns pdfboxing.merge-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.merge :refer :all])
  (:import [java.lang IllegalArgumentException]
           [java.io FileNotFoundException]
           [clojure.lang ArityException]))

;; clean up
(defn clean-up [file]
  (if (.exists (io/as-file file))
    (io/delete-file file)))

(deftest file-argument-check
  (is (thrown? ArityException (file-arg-check)))
  (is (thrown? ArityException (file-arg-check "foo")))
  (testing "minimum number PDF files"
    (is (true? (file-arg-check "foo" "bar"))))
  (testing "more than minimum PDF files"
    (is (true? (file-arg-check "foo" "bar" "baz")))))

(deftest arguments-passed-in
  (testing "when no arguments are supplied"
    (is (thrown? ArityException (merge-pdfs))))
  (testing "when not enough arguments are supplied"
    (is (thrown? ArityException (merge-pdfs "foo" "bar1")))))

(clean-up "foo.pdf")

(deftest pdf-merge-exit-status
        (is (nil? (merge-pdfs "foo.pdf"
                              "pdfs/clojure-1.pdf"
                              "pdfs/clojure-2.pdf"))))

(clean-up "foo.pdf")

(deftest pdf-file-merge
  (let [file "foo.pdf"
        merging-outcome (merge-pdfs file
                                    "pdfs/lpa.pdf"
                                    "pdfs/HelloWorld.pdf")
        merged-pdf-file (.exists (io/as-file file))]
    (is (true? merged-pdf-file))))

(clean-up "foo.pdf")
