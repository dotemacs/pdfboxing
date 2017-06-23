(ns pdfboxing.split
  (:require [pdfboxing.common :as common]
            [pdfboxing.merge :as merge])
  (:import [org.apache.pdfbox.multipdf PDFMergerUtility Splitter]))

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

(defn pddocument->byte-array
  "Given a PDDocument, produce an InputStream"
  [doc]
  (with-open [stream (java.io.ByteArrayOutputStream.)]
    (.save doc stream)
    (.toByteArray stream)))

(defn byte-array->input-stream
  [ba]
  (java.io.ByteArrayInputStream. ba))

(defn pddocument->input-stream
  [doc]
  (byte-array->input-stream (pddocument->byte-array doc)))

(defn merge-pddocuments
  "Given a list of PDDocument, merge them and save."
  [& {:keys [docs output]}]
  (let [merger (PDFMergerUtility.)
        sources (map pddocument->input-stream docs)]
    (.addSources merger sources)
    (.setDestinationFileName merger output)
    (.mergeDocuments merger)))

(defn split-pdf
  "split pdf into pages"
  [& {:keys [input start end split]}]
  {:pre [(arg-check input start end split)]}
  (with-open [doc (common/obtain-document input)]
    (let [splitter (Splitter.)]
      (when start (.setStartPage splitter start))
      (when end (.setEndPage splitter end))
      (when split (.setSplitAtPage splitter split))
      (into [] (.split splitter doc)))))

(defn split-pdf-at
  "splits a pdf into two documents and writes them to disk"
  [& {:keys [input split]}]
  (let [base-name (first (clojure.string/split input #".pdf"))
        f-names (for [x (range 1 3)] (str base-name "-" x ".pdf"))
        pages (split-pdf :input input)
        doc-1 (take (or split (/ (count pages) 2)) pages)
        doc-2 (drop (or split (/ (count pages) 2)) pages)]
    (map #(merge-pddocuments :docs %1 :output %2) [doc-1 doc-2] f-names)))
