(ns pdfboxing.info
  (:import
   [org.apache.pdfbox.pdmodel PDDocument PDDocumentCatalog PDDocumentInformation]))

(defn page-number
  "return number of pages of a PDF document"
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (.getNumberOfPages doc)))
