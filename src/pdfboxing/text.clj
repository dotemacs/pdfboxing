(ns pdfboxing.text
  (:require [pdfboxing.common :as common])
  (:import (org.apache.pdfbox.text PDFTextStripper
                                   PDFTextStripperByArea)
           (java.awt Rectangle)))

(defn extract
  "get text from a PDF document"
  [pdfdoc]
  (with-open [doc (common/obtain-document pdfdoc)]
    (-> (PDFTextStripper.)
        (.getText doc))))

(defn- area-text [doc {:keys [x y w h page-number]
                       :or {x 0 y 0 w 0 h 0 page-number 0}}]
  (let [rectangle    (Rectangle. x y w h)
        pdpage       (.getPage doc page-number)
        textstripper (doto (PDFTextStripperByArea.)
                       (.addRegion "region" rectangle)
                       (.extractRegions pdpage))]
    (.getTextForRegion textstripper "region")))

(defn extract-by-areas
  "get text from specified areas of a PDF document"
  [pdfdoc areas]
  (with-open [doc (common/obtain-document pdfdoc)]
    (reduce (fn [v area] (conj v (area-text doc area))) [] areas)))
