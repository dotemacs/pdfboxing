(ns pdfboxing.merge
  (:import [org.apache.pdfbox.pdmodel PDDocument PDPage]
           [org.apache.pdfbox.util PDFMergerUtility]
           [java.io File FileInputStream]
           [clojure.lang ArityException]))

(defn file-arg-check [& files]
  (if (empty? files)
    (throw (ArityException. 0 "file-count, at least two arguments required"))
    (if (string? files)
      (throw (ArityException. 1 "file-count, at least two arguments required"))
      (when (sequential? files)
        (if (< (count (into [] (flatten files))) 2)
          (throw (ArityException. 1 "file-count, at least two arguments required")))
            true))))

(defn merge-pdfs
  "merge multiple PDFs into output file"
  [output & files]
  {:pre [(file-arg-check files)]}
  (let [merger (PDFMergerUtility.)]
      (doseq [f files]
        (.addSource merger (FileInputStream. (File. f))))
      (.setDestinationFileName merger output)
    (.mergeDocuments merger)))
