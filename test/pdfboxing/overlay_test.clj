(ns pdfboxing.overlay-test
  (:require
    [clojure.java.io :as io]
    [clojure.test :refer [deftest is]]
    [pdfboxing.common :as common]
    [pdfboxing.overlay :refer [overlay-pdf]]))

(def output-file-path "test/pdfs/foo.pdf")

(deftest pdf-file-merge
  (let [_
        (overlay-pdf
          {:input-file-path "test/pdfs/clojure-1.pdf"
           :overlay-file-path "test/pdfs/clojure-2.pdf"
           :output-file-path output-file-path})

        overlayed-pdf-file (.exists (io/as-file output-file-path))]
    (is (true? overlayed-pdf-file))
    (is (true? (common/is-pdf? output-file-path)))))

;; clean up
(defn clean-up [file]
  (if (.exists (io/as-file file))
    (io/delete-file file)))

(deftest cleaner
  (clean-up "test/pdfs/foo.pdf"))
