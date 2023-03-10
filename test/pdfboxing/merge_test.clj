(ns pdfboxing.merge-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest is]]
            [pdfboxing.common :as common]
            [pdfboxing.merge :refer [arg-check merge-pdfs]]))

(deftest input-output-argument-check
  #_:clj-kondo/ignore
  (is (thrown? IllegalArgumentException (arg-check)))
  #_:clj-kondo/ignore
  (is (thrown? IllegalArgumentException (arg-check nil)))
  #_:clj-kondo/ignore
  (is (thrown? IllegalArgumentException (arg-check "input")))
  (is (thrown? IllegalArgumentException (arg-check nil nil)))
  (is (thrown? IllegalArgumentException (arg-check "input" "output")))
  (is (true? (arg-check "output" ["test/pdfs/clojure-1.pdf" "test/pdfs/clojure-2.pdf"])))
  (with-open [output (io/output-stream "test/pdfs/foobar.pdf")
              input-1 (io/input-stream "test/pdfs/clojure-1.pdf")
              input-2 (io/input-stream "test/pdfs/clojure-2.pdf")]
    (is (true? (arg-check output [input-1 input-2])))))


(deftest pdf-merge-exit-status
        (is (nil? (merge-pdfs :output "test/pdfs/foo.pdf"
                              :input ["test/pdfs/clojure-1.pdf" "test/pdfs/clojure-2.pdf"]))))

(deftest pdf-file-merge-paths
  (let [file "test/pdfs/foo.pdf"
        _merging-outcome (merge-pdfs :output file
                                     :input ["test/pdfs/clojure-1.pdf" "test/pdfs/clojure-2.pdf"])
        merged-pdf-file (.exists (io/as-file file))]
    (is (true? merged-pdf-file))
    (is (true? (common/is-pdf? file)))))

(deftest pdf-file-merge-streams
  (let [output-path "test/pdfs/bar.pdf"]
    (with-open [output (io/output-stream output-path)
                input-1 (io/input-stream "test/pdfs/clojure-1.pdf")
                input-2 (io/input-stream "test/pdfs/clojure-2.pdf")]
      (let [_merging-outcome (merge-pdfs :output output
                                         :input [input-1 input-2])
            merged-pdf-file (.exists (io/as-file output-path))]
        (is (true? merged-pdf-file))
        (is (true? (common/is-pdf? output-path)))))))

(deftest pdf-file-merge-files
  (let [file "test/pdfs/foo.pdf"
        _merging-outcome (merge-pdfs :output file
                                     :input [(io/as-file "test/pdfs/clojure-1.pdf")
                                             (io/as-file "test/pdfs/clojure-2.pdf")])
        merged-pdf-file (.exists (io/as-file file))]
    (is (true? merged-pdf-file))
    (is (true? (common/is-pdf? file)))))

;; clean up
(defn clean-up [file]
  (when (.exists (io/as-file file))
    (io/delete-file file)))

(deftest cleaner
  (clean-up "test/pdfs/foo.pdf")
  (clean-up "test/pdfs/bar.pdf")
  (clean-up "test/pdfs/foobar.pdf"))
