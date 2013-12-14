(ns pdfboxing.form
  (:import
    [org.apache.pdfbox.pdmodel PDDocument PDDocumentCatalog]
    [org.apache.pdfbox.pdmodel.common COSObjectable]
    [org.apache.pdfbox.pdmodel.interactive.form PDAcroForm PDAppearance PDField PDSignatureField]))

(defn get-fields
  "get all the field names of a PDF document"
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)
          fields (.getFields form)]
    (vec (map #(.getPartialName %) fields)))))

(defn get-fields-and-values
  "get all the field names and their values from a PDF document"
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (let [catalog (.getDocumentCatalog doc)
          form (.getAcroForm catalog)
          fields (.getFields form)]
      (into {} (map #(hash-map (.getPartialName %) (str (.getValue %))) fields)))))
