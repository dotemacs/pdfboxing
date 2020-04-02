(ns pdfboxing.image
  (:require [clojure.spec.alpha :as s]
            [pdfboxing.common :as common]
            [pdfboxing.info :as info])
  (:import [org.apache.pdfbox.rendering PDFRenderer ImageType]
           [org.apache.pdfbox.pdmodel PDDocument]
           [java.awt.image BufferedImage]))

(s/def ::input #(or (instance? PDDocument %)
                    (common/is-pdf? %)))
(s/def ::dpi int?)
(s/def ::page-idx int?)
(s/def ::export-to-image-config
  (s/keys :req-un [::input]
          :opt-un [::dpi ::page-idx]))

(s/def ::export-to-image-ret #(instance? BufferedImage %))

(defn- page-idx-in-bounds
  [page-idx input]
  (if (<= 0 page-idx (dec (info/page-number input)))
    true
    (common/throw-exception "Page index out of bounds")))

(defn export-to-image
  "Export PDF or PDDocument into BufferedImage
  Only one page will be exported (first by default).
  Split the document first if you want one image for each page."
  [& {:keys [input dpi page-idx]
      :or {dpi 300 page-idx 0}
      :as config}]
  {:pre [(s/valid? ::export-to-image-config config)
         (page-idx-in-bounds page-idx input)]
   :post [(s/valid? ::export-to-image-ret %)]}
  (with-open [doc (common/obtain-document input)]
    (.renderImageWithDPI (PDFRenderer. doc) page-idx dpi ImageType/RGB)))
