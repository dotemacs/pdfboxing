(ns pdfboxing.split
  (:require [pdfboxing.common :as common]
            [pdfboxing.merge :as merge]
            [pdfboxing.info :as info])
  (:import [org.apache.pdfbox.util Splitter]
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
  "Produces a seq of two PDDocuments from :input by splitting the document at :split-page."
  [& {:keys [input split-page]}]
  (let [total-pages (info/page-number input)
        doc-1 (split-pdf :input input :end split-page :split total-pages)
        doc-2 (split-pdf :input input :start (inc split-page) :split total-pages)]    
     (concat doc-1 doc-2)))

;; Works half the time. Why???
(defn split-pdf->disk
  "Save the split PDDocuments to disk"
  [& {:keys [input output]}]
  (let [base-name (if (.endsWith output ".pdf")
                    (apply str (drop-last 4 output))
                    output)
        f-names (for [x (range (count input))] (str base-name "-" x ".pdf"))
        write-document (fn [doc filename]
                         (with-open [output (FileOutputStream. filename)
                                     writer (COSWriter. output)]
                           (.write writer doc)))]
    (map write-document input f-names)))

#_(split-pdf->disk :input (split-pdf-at :input "C:/Users/xb0g/Desktop/2014_Q4.pdf" :split-page 5) :output "C:/Users/xb0g/Desktop/test.pdf")
