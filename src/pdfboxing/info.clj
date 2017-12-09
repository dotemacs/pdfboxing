(ns pdfboxing.info
  (:require [clojure.string :as string]
            [pdfboxing.common :as common])
  (:import clojure.lang.Reflector))

(defn page-number
  "return number of pages of a PDF document"
  [pdfdoc]
  (with-open [doc (common/obtain-document pdfdoc)]
    (.getNumberOfPages doc)))


(defn- getter
  "private helper function to be used by about-doc"
  [obj method-str]
  (let [get-method (str "get"
                        (string/join
                         (map string/capitalize
                              (string/split method-str #"-"))))]
    (Reflector/invokeInstanceMethod obj get-method (to-array []))))

(defn about-doc
  "get the basic information about the given document
   the values provided are for the fields defined by
   info-fields"
  [pdfdoc & {:keys [keys]
             :or {keys ["title" "author" "subject" "keywords"
                        "creator" "producer" "trapped" "metadata-keys"
                        "creation-date" "modification-date"]}}]
  (with-open [doc (common/obtain-document pdfdoc)]
    (let [info (.getDocumentInformation doc)
          info-fields keys]
      (into {}
            (map #(hash-map (str %1) (getter info %1)) info-fields)))))

(defn metadata-value
  "get the value of a custom metadata information field for the document."
  [pdfdoc field-name]
  (with-open [doc (common/obtain-document pdfdoc)]
    (.. doc getDocumentInformation (getCustomMetadataValue field-name))))

(defn metadata-values
  "get all values of a custom metadata information field for the document."
  [pdfdoc]
  (with-open [doc (common/obtain-document pdfdoc)]
    (let [info (.. doc getDocumentInformation)]
      (into {}
            (map #(hash-map (str %1) (.. info (getCustomMetadataValue %1)))
                 (.. info getMetadataKeys))))))
