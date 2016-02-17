(ns pdfboxing.text
  (:require [pdfboxing.common :as common])
  (:import [org.apache.pdfbox.util PDFTextStripper]))

(defn extract-from-pddocument
  "get text from a PDDocument"
  [pddoc]
    (if (instance? org.apache.pdfbox.pdmodel.PDDocument pddoc)
      (let [stripper (new PDFTextStripper)]
        (.getText stripper pddoc))))

(defn extract
  "get text from a PDF document filesystem path"
  [pdfdoc]
  (with-open [doc (common/load-pdf pdfdoc)]
    (extract-from-pddocument doc)))
