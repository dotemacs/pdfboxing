(ns pdfboxing.common
  (:require [clojure.java.io :as io])
  (:import (java.io File)
           (org.apache.pdfbox Loader)
           (org.apache.pdfbox.io RandomAccessReadBufferedFile)))


(defn load-pdf-from-media [pdf-file-or-path]
  (try
    (-> pdf-file-or-path
        ^File (io/as-file)
        (Loader/loadPDF))
    (catch Exception _)))

(defn load-pdf-from-bytes [^bytes pdf-bytes]
  (Loader/loadPDF pdf-bytes))

(defn try-get-as-pdf
  "Try and get the pdf-file-or-path as a PDF.
  Returns nil if pdf-file-or-path could not be loaded as a PDF."
  [pdf-file-or-path]
  (try
    (-> pdf-file-or-path
        ^File (io/as-file)
        (RandomAccessReadBufferedFile.)
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

(defn load-pdf
  "Load a given PDF only after checking if it really is a PDF"
  [bytes-pdf-file-or-path]
  (if-let [pdf (try-get-as-pdf bytes-pdf-file-or-path)]
    pdf
    (throw (IllegalArgumentException. (format "%s is not a PDF file" bytes-pdf-file-or-path)))))

(defprotocol PDFDocument
  "return an object from which text can be extracted"
  (obtain-document [source]))

(extend-protocol PDFDocument
  (Class/forName "[B")                                      ;; byte-array
  (obtain-document [source]
    (load-pdf-from-bytes source))

  String
  (obtain-document [source]
    (load-pdf-from-media source))

  File
  (obtain-document [source]
    (load-pdf-from-media source)))

(defn get-form
  "Obtain AcroForm from a open `doc`, opened with obtain-document"
  [doc]
  (-> doc
      .getDocumentCatalog
      .getAcroForm))
