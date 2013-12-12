(ns pdfboxing.merge-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.merge :refer :all])
  (:import [java.io FileNotFoundException]
           [clojure.lang ArityException]))

(deftest input-output-argument-check
  (is (thrown? IllegalArgumentException (arg-check)))
  (is (thrown? IllegalArgumentException (arg-check "input")))
  (is (thrown? IllegalArgumentException (arg-check "input" "output")))
  (is (true? (arg-check "output" ["a.pdf" "b.pdf"]))))


(deftest pdf-merge-exit-status
        (is (nil? (merge-pdfs :output "foo.pdf"
                              :input ["pdfs/clojure-1.pdf" "pdfs/clojure-2.pdf"]))))

(deftest pdf-file-merge
  (let [file "foo.pdf"
        merging-outcome (merge-pdfs :output file
                                    :input ["pdfs/clojure-1.pdf" "pdfs/clojure-2.pdf"])
        merged-pdf-file (.exists (io/as-file file))]
    (is (true? merged-pdf-file))))

;; clean up
(defn clean-up [file]
  (if (.exists (io/as-file file))
    (io/delete-file file)))

(deftest cleaner
  (clean-up "foo.pdf"))
