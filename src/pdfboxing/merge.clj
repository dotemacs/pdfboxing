(ns pdfboxing.merge
  (:require [pdfboxing.common :as common])
  (:import (java.io File InputStream OutputStream)
           (org.apache.pdfbox.io IOUtils RandomAccessRead RandomAccessReadBuffer)
           (org.apache.pdfbox.multipdf PDFMergerUtility)
           (org.apache.pdfbox.pdmodel PDDocument
                                      PDPage PDPageContentStream)
           (org.apache.pdfbox.pdmodel.common PDRectangle)
           (org.apache.pdfbox.pdmodel.graphics.image PDImageXObject)))

(defn throw-exception
  [^String message]
  (throw (IllegalArgumentException. message)))

(defn check-if-present
  "Check if the input & output file names where supplied"
  [input output]
  (when (or (nil? input) (nil? output))
    (throw-exception "argument can't be empty")))

(defn input-stream-or-pdf?
  "Checks if the file supplied is a PDF or an InputStream"
  [file]
  (or (instance? InputStream file)
      (common/is-pdf? file)))

(defn check-for-pdfs
  "Check if all the files supplied are actual PDFs."
  [files]
  (if (some false? (map input-stream-or-pdf? files))
    (throw-exception "the files supplied need to be PDFs")
    true))

(defn arg-check [output input]
  (check-if-present input output)
  (if (sequential? input)
    (check-for-pdfs input)
    (throw-exception "input - needs to be sequential")))

(defn merge-pdfs
  "merge multiple PDFs into output file"
  [& {:keys [output input]}]
  {:pre [(arg-check output input)]}
  (let [merger (PDFMergerUtility.)]
    (doseq [source input]
      (condp instance? source
        File (.addSource merger ^File source)
        String (.addSource merger ^String source)
        InputStream (.addSource merger ^RandomAccessRead
                                (RandomAccessReadBuffer. ^InputStream source))))
    (cond
      (instance? OutputStream output)
      (.setDestinationStream merger output)

      :else
      (.setDestinationFileName merger output))
    (.mergeDocuments merger (IOUtils/createMemoryOnlyStreamCache))
    (condp instance? output
      File (.close output)
      OutputStream (.close output)
      nil)))

(defn- add-image-to-page
  "Adds image as a page to the document object"
  [^PDDocument doc ^PDImageXObject image]
  (let [page-size PDRectangle/A4
        original-width (.getWidth image)
        original-height (.getHeight image)
        page-width (.getWidth page-size)
        page-height (.getHeight page-size)
        ratio (min (/ page-width original-width) (/ page-height original-height))
        scaled-width (* original-width ratio)
        scaled-height (* original-height ratio)
        x (/ (- page-width scaled-width) 2)
        y (/ (- page-height scaled-height) 2)
        page (PDPage. page-size)]
    (.addPage doc page)
    (with-open [stream (PDPageContentStream. doc page)]
      (.drawImage stream image x y scaled-width scaled-height))))

(defn merge-images-from-byte-array
  "Merges images provided as a vector of byte arrays into one PDF"
  [images output-doc]
  (with-open [doc (PDDocument.)]
    (run! #(add-image-to-page doc (PDImageXObject/createFromByteArray doc % nil)) images)
    (.save doc output-doc)))

(defn merge-images-from-path
  "Merges images provided as a vector of string paths"
  [images output-doc]
  (with-open [doc (PDDocument.)]
    (run! #(add-image-to-page doc (PDImageXObject/createFromFile % doc))
          images)
    (.save doc output-doc)))
