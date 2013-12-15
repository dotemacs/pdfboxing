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
          (str "Error: non existent field provided")
          )))))
