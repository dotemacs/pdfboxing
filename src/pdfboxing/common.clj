(ns pdfboxing.common
  (:require [clojure.java.io :as io])
  (:import (java.io File)
           (org.apache.pdfbox.pdmodel PDDocument)
           (org.apache.pdfbox.io RandomAccessFile)
           (org.apache.pdfbox.pdfparser PDFParser)))

(defn try-get-as-pdf
  "Try and get the pdf-file-or-path as a PDF.
  Returns nil if pdf-file-or-path could not be loaded as a PDF."
  [pdf-file-or-path]
  (let [^File pdf-file (io/as-file pdf-file-or-path)
        random-access-file (RandomAccessFile. pdf-file "r")
        parser (PDFParser. random-access-file)]
    (try
      (.parse parser)
      (.getPDDocument parser)
      (catch Exception _))))

(defn is-pdf?
  "Confirm that the PDF supplied is really a PDF"
  [pdf-file-or-path]
  (if-let [pdf (try-get-as-pdf pdf-file-or-path)]
    (try
      (not (nil? pdf))
      (finally
        (.close pdf)))
    false))

(defn load-pdf
  "Load a given PDF only after checking if it really is a PDF"
  [pdf-file-or-path]
  (if-let [pdf (try-get-as-pdf pdf-file-or-path)]
    pdf
    (throw (IllegalArgumentException. (format "%s is not a PDF file" pdf-file-or-path)))))

(defprotocol PDFDocument
  "return an object from which text can be extracted"
  (obtain-document [source]))

(extend-protocol PDFDocument
  String
  (obtain-document [source]
    (load-pdf source))

  File
  (obtain-document [source]
    (load-pdf source))

  PDDocument
  (obtain-document [source]
    source))

(defn get-form
  "Obtain AcroForm from a open `doc`, opened with obtain-document"
  [doc]
  (-> doc
      .getDocumentCatalog
      .getAcroForm))
