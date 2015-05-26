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
  [& {:keys [input start end] }]
  {:pre [(arg-check input start end)]}
  (let [splitter (Splitter.)]
    (when start (.setStartPage splitter start))
    (when end (.setEndPage splitter end))
    (.split splitter (common/load-pdf input))))

(defn split-pdf-at
  "split a pdf into 2 PDDocuments. This is akin to take n / drop n with the results being merged."
  [& {:keys [input split-page]}]
  (let [take-pages (split-pdf :input input :end split-page)
        drop-pages (split-pdf :input input :start (inc split-page))
        merged-take (merge/merge-pddocuments :input take-pages)
        merged-drop (merge/merge-pddocuments :input drop-pages)]
    [merged-take merged-drop]))

(defn save-pddocuments
  "save a collection of PDDocument to disk"
  [& {:keys [output input]}]
  (let [base-path (if (= ".pdf" (str (take-last 4 output)))
                    (str (drop-last 4 output))
                    output)
        file-paths (for [x (range 0 (count input))] (str base-path "-" x ".pdf"))]
    (map #(.save %1 %2) input file-paths)
    (apply #(.close %) input)))

#_(save-pddocuments :output "C:/Users/xb0g/Desktop/test-again.pdf" :input (split-pdf-at :input "c:/Users/xb0g/Desktop/2014_Q4.pdf" :split-page 5))
