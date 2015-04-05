(ns pdfboxing.merge-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.merge :refer :all]
            [pdfboxing.common :as common]))

(deftest input-output-argument-check
  (is (thrown? IllegalArgumentException (arg-check)))
  (is (thrown? IllegalArgumentException (arg-check "input")))
  (is (thrown? IllegalArgumentException (arg-check "input" "output")))
  (is (true? (arg-check "output" ["test/pdfs/clojure-1.pdf" "test/pdfs/clojure-2.pdf"]))))


(deftest pdf-merge-exit-status
        (is (nil? (merge-pdfs :output "test/pdfs/foo.pdf"
                              :input ["test/pdfs/clojure-1.pdf" "test/pdfs/clojure-2.pdf"]))))

(deftest pdf-file-merge
  (let [file "test/pdfs/foo.pdf"
        merging-outcome (merge-pdfs :output file
                                    :input ["test/pdfs/clojure-1.pdf" "test/pdfs/clojure-2.pdf"])
        merged-pdf-file (.exists (io/as-file file))]
    (is (true? merged-pdf-file))
    (is (true? (common/is-pdf? file)))))

;; clean up
(defn clean-up [file]
  (if (.exists (io/as-file file))
    (io/delete-file file)))

(deftest cleaner
  (clean-up "test/pdfs/foo.pdf"))
