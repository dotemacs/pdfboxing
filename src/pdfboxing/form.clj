(ns pdfboxing.form
  (:import
    [org.apache.pdfbox.pdmodel PDDocument PDDocumentCatalog]
    [org.apache.pdfbox.pdmodel.common COSObjectable]
    [org.apache.pdfbox.pdmodel.interactive.form PDAcroForm PDAppearance PDField PDSignatureField]
    [java.io IOException]))

(defn get-fields
  "get all the field names and their values from a PDF document"
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)
          fields (.getFields form)]
      (into {} (map #(hash-map (.getPartialName %) (str (.getValue %))) fields)))))
    
(defn get-checkbox-field-options
  "get the On and Off values for all Fields of type \"Btn\""
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)
          fields (.getFields form)
          checkboxes (filter #(= "Btn" (.getFieldType %)) fields)]
      (into {} (map #(hash-map (.getPartialName %) {:on (str (.getOnValue %)) :off (.getOffValue %)}) checkboxes)))))

(defn set-fields
  "fill in the fields with the values provided"
  [input output new-fields]
  (with-open [doc (PDDocument/load input)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)]
      (try
        (do
          (doseq [field new-fields]
            (.setValue (.getField form (name (key field))) (val field)))
          (.save doc output))
        (catch IOException e
          (str "Error: can't add non-simple fonts, this is a constraint of PDFBox."))
        (catch NullPointerException e
          (str "Error: non existent field provided"))))))

(defn rename-fields
  "take an input PDF, a new file to be created and a map with keys as
  the current form field names and values as the new names, and rename
  them"
  [input output fields-map]
  (with-open [doc (PDDocument/load input)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)]
      (doseq [field fields-map]
        (.setPartialName
         (.getField form (str (first field)))
         (str (last field))))
      (.save doc output))))
