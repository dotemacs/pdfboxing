(ns pdfboxing.security-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [pdfboxing.security :refer :all]))

(deftest document-info
  (testing "password protection"
    (let [ofile "test/pdfs/encrypted.pdf"]
      (protect-doc "test/pdfs/interactiveform.pdf" "12345"
                   :user-password "abc"
                   :output-pdf ofile)
      (is (.exists (io/as-file ofile)))
      (when (.exists (io/as-file ofile))
        (io/delete-file ofile)))))
