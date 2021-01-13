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

(defn- area-text [doc {:keys [x y w h page-number]}]
  (let [page-number  (or page-number 0)
        rectangle    (Rectangle. x y w h)
        pdpage       (.getPage doc page-number)
        textstripper (doto (PDFTextStripperByArea.)
                       (.addRegion "region" rectangle)
                       (.extractRegions pdpage))]
    (.getTextForRegion textstripper "region")))

(defn extract-by-areas
  "get text from specified areas of a PDF document"
  [pdfdoc areas]
  (with-open [doc (common/obtain-document pdfdoc)]
    (doall (map #(area-text doc %) areas))))
