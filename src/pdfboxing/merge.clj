(ns pdfboxing.merge
  (:import [org.apache.pdfbox.util PDFMergerUtility]
           [java.io File FileInputStream]))

(defn arg-check [output input]
  (if (empty? output)
    (throw (IllegalArgumentException. "output - required argument"))
    (if (empty? input)
      (throw (IllegalArgumentException. "input - required argument"))
      (if (sequential? input)
        true
        (throw (IllegalArgumentException. "input - needs to be sequential"))))))

(defn merge-pdfs
  "merge multiple PDFs into output file"
  [& {:keys [output input]}]
  {:pre [(arg-check output input)]}
  (let [merger (PDFMergerUtility.)]
      (doseq [f input]
        (.addSource merger (FileInputStream. (File. f))))
      (.setDestinationFileName merger output)
    (.mergeDocuments merger)))
