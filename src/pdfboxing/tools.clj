(ns pdfboxing.tools
  (:import [org.apache.pdfbox.tools ExtractText]))


(defn pdf-to-html
  "Convert a PDF document to a simple HTML file.
  `pdf-doc` - string, name of a PDF document that is in the root folder
  this is command line tool from pdfbox more info at:
  https://pdfbox.apache.org/2.0/commandline.html#extracttext"
  [pdf-doc]
  (ExtractText/main (into-array String ["-html" pdf-doc])))
