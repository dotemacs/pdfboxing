(ns pdfboxing.info
  (:require [clojure.string :as string]
            [pdfboxing.common :as common])
  (:import [clojure.lang Reflector]
           [org.apache.pdfbox.pdmodel PDDocumentCatalog PDDocumentInformation]))

(defn page-number
  "return number of pages of a PDF document"
  [pdfdoc]
  (with-open [doc (common/load-pdf pdfdoc)]
    (.getNumberOfPages doc)))


(defn- getter
  "private helper function to be used by about-doc"
  [obj method-str]
  (let [get-method (str "get" (string/join (map string/capitalize (string/split method-str #"-"))))]
    (Reflector/invokeInstanceMethod obj get-method (to-array []))))

(defn about-doc
  "get the basic information about the given document
   the values provided are for the fields defined by
   info-fields"
  [pdfdoc]
  (with-open [doc (common/load-pdf pdfdoc)]
    (let [info (.getDocumentInformation doc)
          info-fields ["title" "author" "subject" "keywords" "creator" "producer" "trapped" "metadata-keys" "creation-date" "modification-date"]]
      (into {}
            (map #(hash-map (str %1) (getter info %1)) info-fields)))))

(defn metadata-value
  "get the value of a custom metadata information field for the document."
  [pdfdoc field-name]
  (with-open [doc (common/load-pdf pdfdoc)]
    (.. doc getDocumentInformation (getCustomMetadataValue field-name))))
