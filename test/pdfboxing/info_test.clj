(ns pdfboxing.info-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.info :refer :all]))

(deftest document-info
  (is (= 1 (page-number "test/pdfs/interactiveform.pdf"))))
