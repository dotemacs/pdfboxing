(ns pdfboxing.text
  (:import [org.apache.pdfbox.pdmodel PDDocument]
           [org.apache.pdfbox.util PDFTextStripper]))

(defn extract
  "get text from a PDF document"
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (let [stripper (new PDFTextStripper)]
      (.getText stripper doc))))
