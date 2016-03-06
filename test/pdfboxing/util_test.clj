(ns pdfboxing.util-test
  (:require [clojure.test :refer :all]
            [pdfboxing.util :refer :all]))

(deftest first-line-validation
  (testing "the contents of the first line"
    (is (false? (first-line-valid? "%PDF-2")))
    (is (true? (first-line-valid? "%PDF-1.9")))))

(deftest lenght-of-line
  (testing "line length of the second line of a given PDF file"
    (is (false? (line-long-enough? "123456789")))
    (is (true? (line-long-enough? "12345")))))

(deftest first-char-of-a-string
  (testing "the start of the second line"
    (is (false? (first-char-is-percent? (char \f))))
    (is (true? (first-char-is-percent? (char \%))))))

(deftest bytes-validation
  (testing "if passed in bytes are greater than 127"
    (is (true? (valid-bytes? [0xff 0xe1 0xe9 0xeb 0xd3])))
    (is (false? (valid-bytes? [0x11 0xe1 0xe9 0xeb 0xd3])))))

(deftest line-content-validation
  (let [second-line (second (line-seq (clojure.java.io/reader "test/pdfs/clojure-1.pdf")))]
    (testing "if the line supplied is valid"
      (is (true? (valid-line-content? second-line)))
      (is (false? (valid-line-content? (clojure.string/replace second-line #"%" "!")))))))
