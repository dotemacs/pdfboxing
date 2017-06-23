(ns pdfboxing.text-test
  (:require [clojure.test :refer [deftest is]]
            [pdfboxing.text :refer [extract]]))

(def line-separator (System/getProperty "line.separator"))

(deftest text-extraction
  (is (= (str "Hello, this is pdfboxing.text" line-separator)
         (extract "test/pdfs/hello.pdf"))))
