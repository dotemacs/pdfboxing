(ns pdfboxing.split
  (:require [pdfboxing.common :as common]
            [pdfboxing.merge :as merge])
  (:import [org.apache.pdfbox.util Splitter]
           [java.io File FileInputStream]))

(defn check-if-integer
  [coll]
  (if (every? integer? coll)
    true
    (merge/throw-exception ":start and :end may only be integers")))


(defn arg-check [input start end]
  (let [int-args [start end]]
    (if (string? input)
      (merge/check-for-pdfs  [input])
      (merge/throw-exception "input must be a string"))
    (check-if-integer (filter (complement nil?) int-args))))

(defn split-pdf
  "split pdf into pages"
  [{:keys [input start end] }]
  {:pre [(arg-check input start end)]}
  (let [splitter (Splitter.)]
    (when start (.setStartPage splitter start))
    (when end (.setEndPage splitter end))
    (.split splitter (common/load-pdf input))))
