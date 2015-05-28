(ns pdfboxing.split
  (:require [pdfboxing.common :as common]
            [pdfboxing.merge :as merge]
            [pdfboxing.info :as info])
  (:import [org.apache.pdfbox.util Splitter PDFMergerUtility]
           [org.apache.pdfbox.pdfwriter COSWriter]
           [java.io File FileInputStream FileOutputStream]
           [org.apache.pdfbox.pdmodel PDDocument]))

(defn check-if-integer
  [coll]
  (if (every? integer? coll)
    true
    (merge/throw-exception ":start and :end may only be integers")))


(defn arg-check [input start end split]
  (let [int-args [start end split]]
    (if (string? input)
      (merge/check-for-pdfs  [input])
      (merge/throw-exception "input must be a string"))
    (check-if-integer (filter (complement nil?) int-args))))

(defn write-doc [doc filename]
  (with-open [output (FileOutputStream. filename)
              writer (COSWriter. output)]
    (.write writer doc)))

(defn merge-docs
  [docs]
  (let [merger (PDFMergerUtility.)
        destination (PDDocument.)]
    (doseq [d docs]
      (.appendDocument merger destination d)
      (.close d))
    destination))

(defn split-pdf
  "split pdf into pages"
  [& {:keys [input start end split]}]
  {:pre [(arg-check input start end split)]}
  (let [doc (common/load-pdf input)
        splitter (Splitter.)]
    (when start (.setStartPage splitter start))
    (when end (.setEndPage splitter end))
    (when split (.setSplitAtPage splitter split))
    (.split splitter doc)))

(defn split-pdf-at
  "splits a pdf into two documents and writes them to disk"
  [& {:keys [input split]}]
  (let [base-name (first (clojure.string/split input #".pdf"))
        f-names (for [x (range 1 3)] (str base-name "-" x ".pdf"))
        pages (split-pdf :input input)
        split-page (or split (/ (count pages) 2))
        doc-1 (merge-docs (take split-page pages))
        doc-2 (merge-docs (drop split-page pages))]
    (map write-doc [doc-1 doc-2] f-names)))










