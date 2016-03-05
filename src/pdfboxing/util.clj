(ns pdfboxing.util)

(defn first-line-valid?
  "validate the first line of a given PDF file"
    [first-line]
    (.matches first-line "%PDF-1\\.[1-9]"))

(defn line-long-enough?
  "check the length of the second line of a given PDF file"
    [second-line]
    (>= 5 (.length second-line)))
