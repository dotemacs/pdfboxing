(ns pdfboxing.common
  (:require [clojure.java.io :as io])
  (:import
    [javax.activation FileDataSource]
    [org.apache.pdfbox.pdmodel PDDocument]
    [org.apache.pdfbox.preflight.parser PreflightParser]
    [java.io File]))

(defn is-pdf?
  "Confirm that the PDF supplied is really a PDF"
  [pdf-file-or-path]
  (let [^File pdf-file (io/as-file pdf-file-or-path)
        data-source (FileDataSource. pdf-file)
        parser (PreflightParser. data-source)]
    (try
      (do
        (.parse parser)
        true)
      (catch Exception e false))))

(defn load-pdf
  "Load a given PDF only after checking if it really is a PDF"
  [pdf-file-or-path]
  (let [^File pdf-file (io/as-file pdf-file-or-path)]
    (if (is-pdf? pdf-file)
      (PDDocument/load pdf-file)
      (throw (IllegalArgumentException. (format "%s is not a PDF file" pdf-file))))))

(defprotocol PDFDocument
  "return an object from which text can be extracted"
  (obtain-document [source]))

(extend-protocol PDFDocument
  java.lang.String
  (obtain-document [source]
    (if (is-pdf? source)
      (load-pdf source)))

  java.io.File
  (obtain-document [source]
    (if (is-pdf? source)
      (load-pdf source)))

  org.apache.pdfbox.pdmodel.PDDocument
  (obtain-document [source]
    source))
