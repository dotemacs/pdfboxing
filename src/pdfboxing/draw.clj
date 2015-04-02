(ns pdfboxing.draw
  (:import [org.apache.pdfbox.pdmodel PDDocument PDDocumentCatalog PDPage]
           [org.apache.pdfbox.pdmodel.edit PDPageContentStream]))

(defn get-catalog
  "get a catalog from a PDF document"
  [document]
  (.getDocumentCatalog document))

(defn get-page
  "get a particular page from a catalog"
  [catalog page-number]
  (.get (.getAllPages catalog) page-number))

(defn get-content-stream
  "take the read in PDF document and a page number and return content
  stream which will be manipulated"
  [document page-number]
  (let [catalog (get-catalog document)
        page (get-page catalog page-number)
        content-stream (PDPageContentStream. document page true true)]
    content-stream))

(defn use-rgb-colour
  "Take content-stream and use RGB colour to draw on it.
   By default black colour will be used."
  [content-stream & {:keys [r g b] :or {r 0 g 0 b 0}}]
  (.setStrokingColor content-stream r g b))

(defn set-line-width
  "the size of the line to be drawn
   e.g. 3"
  [content-stream line-width]
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
  "draw a line on input-pdf's page, producing a output-pdf document"
  [& {:keys [input-pdf page-number output-pdf coordinates]}]
  (with-open [document (PDDocument/load input-pdf)]
    (with-open [content-stream (get-content-stream document page-number)]
      (use-rgb-colour content-stream)
      (set-line-width content-stream 3)
      (draw-line-at-coordinates content-stream coordinates))
      (.save document output-pdf)))
