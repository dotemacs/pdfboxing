(ns pdfboxing.common
  (:require [clojure.java.io :as io])
  (:import java.io.File
           javax.activation.FileDataSource
           org.apache.pdfbox.pdmodel.PDDocument
           org.apache.pdfbox.preflight.parser.PreflightParser))

(defn try-get-as-pdf
  "Try and get the pdf-file-or-path as a PDF.
  Returns nil if pdf-file-or-path could not be loaded as a PDF."
  [pdf-file-or-path]
  (let [^File pdf-file (io/as-file pdf-file-or-path)
        data-source (FileDataSource. pdf-file)
        parser (PreflightParser. data-source)]
    (try
      (.parse parser)
      (.getPreflightDocument parser)
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
  java.lang.String
  (obtain-document [source]
    (load-pdf source))

  java.io.File
  (obtain-document [source]
    (load-pdf source))

  org.apache.pdfbox.pdmodel.PDDocument
  (obtain-document [source]
    source))
