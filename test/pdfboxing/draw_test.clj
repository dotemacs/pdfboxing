(ns pdfboxing.draw-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest is]]
            [pdfboxing.common :refer [obtain-document]]
            [pdfboxing.draw :refer [get-page get-catalog]]))

(deftest get-page-from-catalog
  (let [catalog (-> "test/pdfs/hello.pdf"
                    obtain-document
                    get-catalog)]
    (is (not (nil? (get-page catalog 0))))))
