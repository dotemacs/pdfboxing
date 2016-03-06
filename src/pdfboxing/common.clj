(ns pdfboxing.common
  (:require [pdfboxing.util :as util])
  (:import
   [javax.activation FileDataSource]
   [org.apache.pdfbox.pdmodel PDDocument]))

(defn is-pdf?
  "confirm if the given file name is a PDF file"
    [string]
    (let [content (line-seq (clojure.java.io/reader string))
          first-line (first content)
          second-line (second content)]
      (and (util/first-line-valid? first-line)
           (util/second-line-valid? second-line))))

(defn load-pdf
  "Load a given PDF only after checking if it really is a PDF"
  [pdf-file]
  (if (is-pdf? pdf-file)
    (PDDocument/load pdf-file)
    (throw (IllegalArgumentException. (format "%s is not a PDF file" pdf-file)))))
