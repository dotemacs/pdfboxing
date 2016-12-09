(ns pdfboxing.draw
  (:require [pdfboxing.common :as common])
  (:import [org.apache.pdfbox.pdmodel PDDocumentCatalog PDPage PDPageContentStream]))

(defn get-catalog
  "Get a catalog from a PDF document"
  [document]
  (.getDocumentCatalog document))

(defn get-page
  "Get a particular page from a catalog"
  [catalog page-number]
  (.get (.getAllPages catalog) page-number))

(defn get-content-stream
  "Take the read in PDF document and a page number and return content
  stream which will be manipulated"
  [document page-number]
  (let [catalog (get-catalog document)
        page (get-page catalog page-number)
        content-stream (PDPageContentStream. document page true true)]
    content-stream))

(defn use-rgb-colour
  "Take content-stream and use RGB colour to draw on it.
   By default black colour will be used."
  [content-stream & {:keys [r g b]
                     :or {r 0
                          g 0
                          b 0}}]
  (.setStrokingColor content-stream r g b))

(defn set-line-width
  "The width of the line to be drawn.
   By default it's 3."
  [content-stream & {:keys [line-width]
                     :or {line-width 3}}]
  (.setLineWidth content-stream line-width))

(defn draw-line-at-coordinates
  "Take content-stream and coordinates for line to be drawn.
   Coordinates are a hash map in the format of: x, y, x1, y1."
  [content-stream coordinates]
  (.drawLine content-stream
             (:x coordinates)
             (:y coordinates)
             (:x1 coordinates)
             (:y1 coordinates)))

(defn draw-line
  "Draw a line on input-pdf's page, producing a output-pdf document.
   It takes three arguments input-pdf, output-pdf and
   coordinates. Where coordinates have: page-number, x, y, x1, y1
   points where the line should be drawn."
  [& {:keys [input-pdf output-pdf coordinates]}]
  (with-open [document (common/obtain-document input-pdf)]
    (with-open [content-stream (get-content-stream document (:page-number coordinates))]
      (use-rgb-colour content-stream)
      (set-line-width content-stream)
      (draw-line-at-coordinates content-stream coordinates))
      (.save document output-pdf)))
