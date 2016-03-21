(ns pdfboxing.form
  (:require [pdfboxing.common :as common])
  (:import
   [org.apache.pdfbox.pdmodel PDDocumentCatalog]
   [org.apache.pdfbox.pdmodel.common COSObjectable]
   [org.apache.pdfbox.pdmodel.interactive.form PDAcroForm PDAppearance PDField PDSignatureField]
   [java.io IOException]))

(defn get-fields
  "get all the field names and their values from a PDF document"
  [pdfdoc]
  (with-open [doc (common/obtain-document pdfdoc)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)
          fields (.getFields form)]
      (into {} (map #(hash-map (.getPartialName %) (str (.getValue %))) fields)))))

(defn set-fields
  "fill in the fields with the values provided"
  [input output new-fields]
  (with-open [doc (common/obtain-document input)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)]
      (try
        (do
          (doseq [field new-fields]
            (.setValue (.getField form (name (key field))) (val field)))
          (.save doc output))
        (catch NullPointerException e
          (str "Error: non existent field provided"))))))

(defn rename-fields
  "take an input PDF, a new file to be created and a map with keys as
  the current form field names and values as the new names, and rename
  them"
  [input output fields-map]
  (with-open [doc (common/obtain-document input)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)]
      (doseq [field fields-map]
        (.setPartialName
         (.getField form (str (first field)))
         (str (last field))))
      (.save doc output))))
