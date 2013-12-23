(ns pdfboxing.info
  (:require [clojure.string :as string])
  (:import
   [clojure.lang Reflector]
   [org.apache.pdfbox.pdmodel PDDocument PDDocumentCatalog PDDocumentInformation]))

(defn page-number
  "return number of pages of a PDF document"
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (.getNumberOfPages doc)))


(defn- getter
  "private helper function to be used by about-doc"
  [obj method-str & args]
  (let [get-method (str "get" (string/capitalize method-str))]
    (Reflector/invokeInstanceMethod obj get-method (to-array args))))

(def info-fields ["title" "author" "subject" "keywords" "creator" "producer"])

(defn about-doc
  "get the basic information about the given document
   the values provided are for the fields defined by
   info-fields"
  [pdfdoc]
  (with-open [doc (PDDocument/load pdfdoc)]
    (let [info (.getDocumentInformation doc)]
      (into {}
            (map #(hash-map (str %1) (getter info %1)) info-fields)))))
