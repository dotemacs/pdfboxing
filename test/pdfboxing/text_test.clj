(ns pdfboxing.text-test
  (:require [clojure.test :refer :all]
            [pdfboxing.text :refer :all]))

(def line-separator (System/getProperty "line.separator"))

(deftest text-extraction
  (is (= (str "Hello, this is pdfboxing.text" line-separator)
         (extract "test/pdfs/hello.pdf"))))
