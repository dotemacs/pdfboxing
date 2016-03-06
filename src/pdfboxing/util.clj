(ns pdfboxing.util)

(defn first-line-valid?
  "validate the first line of a given PDF file"
    [first-line]
    (.matches first-line "%PDF-1\\.[1-9]"))

(defn line-long-enough?
  "check the length of the second line of a given PDF file"
    [second-line]
    (>= 5 (.length second-line)))

(defn first-char-is-percent?
  "check that the second line of a given PDF starts with a '%'"
  [first-char]
  (= "%" (str first-char)))

(defn valid-bytes?
  "check that the bytes on the second line of a given PDF are larger
  than 127"
  [given-bytes]
  (not-any? false? (map #(< 0x80 (int %)) given-bytes)))

(defn valid-line-content?
  "check if the line given is valid, to be used to verify the second
  line of a PDF file"
    [second-line]
    (and (first-char-is-percent? (char (first (.getBytes second-line))))
         (valid-bytes? (rest second-line))))
