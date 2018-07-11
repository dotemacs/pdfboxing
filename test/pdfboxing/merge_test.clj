(ns pdfboxing.merge-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest is]]
            [pdfboxing.common :as common]
            [pdfboxing.info :refer [page-number]]
            [pdfboxing.merge :refer [arg-check merge-pdfs merge-images-from-path]]))

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

(deftest pdf-image-merge
  (let [file "test/pdfs/image_merge.pdf"
        images ["test/images/image1.jpg" "test/images/image2.jpg"]]
    (merge-images-from-path images file)
    (is (= (page-number file) 2))))

;; clean up
(defn clean-up [file]
  (if (.exists (io/as-file file))
    (io/delete-file file)))

(deftest cleaner
  (clean-up "test/pdfs/foo.pdf")
  (clean-up "test/pdfs/image_merge.pdf"))
