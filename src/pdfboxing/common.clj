(ns pdfboxing.common
  (:require [clojure.java.io :as io])
  (:import (java.io File)
           (org.apache.pdfbox Loader)
           (org.apache.pdfbox.pdmodel PDDocument)))


(defn load-pdf-from-media [pdf-file-or-path]
  (try
    (-> pdf-file-or-path
        ^File (io/as-file)
        (Loader/loadPDF))
    (catch Exception _)))

(defn is-pdf?
  "Confirm that the PDF supplied is really a PDF"
  [pdf-file-or-path]
  (if-let [pdf (load-pdf-from-media pdf-file-or-path)]
    (try
      (not (nil? pdf))
      (finally
        (.close pdf)))
    false))

(defprotocol PDFDocument
  "return an object from which text can be extracted"
  (obtain-document [source]))

(extend-protocol PDFDocument
  (Class/forName "[B") ;; byte-array
  #_{:clj-kondo/ignore [:function-name]}
  (obtain-document [source]
    (Loader/loadPDF source))

  String
  (obtain-document [source]
    (load-pdf-from-media source))

  File
  (obtain-document [source]
    (load-pdf-from-media source))

  PDDocument
  (obtain-document [source]
    source))

(defn get-form
  "Obtain AcroForm from a open `doc`, opened with obtain-document"
  [doc]
  (-> doc
      .getDocumentCatalog
      .getAcroForm))
