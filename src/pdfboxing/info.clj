(ns pdfboxing.info
  (:require [clojure.string :as string]
            [pdfboxing.common :as common])
  (:import [clojure.lang Reflector]
           [org.apache.pdfbox.pdmodel PDDocumentCatalog PDDocumentInformation]
           [org.apache.pdfbox.pdmodel.encryption
            AccessPermission StandardProtectionPolicy]))

(defn page-number
  "return number of pages of a PDF document"
  [pdfdoc]
  (with-open [doc (common/load-pdf pdfdoc)]
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
  (with-open [doc (common/load-pdf pdfdoc)]
    (let [info (.getDocumentInformation doc)
          info-fields keys]
      (into {}
            (map #(hash-map (str %1) (getter info %1)) info-fields)))))

(defn metadata-value
  "get the value of a custom metadata information field for the document."
  [pdfdoc field-name]
  (with-open [doc (common/load-pdf pdfdoc)]
    (.. doc getDocumentInformation (getCustomMetadataValue field-name))))

(defn metadata-values
  "get all values of a custom metadata information field for the document."
  [pdfdoc]
  (with-open [doc (common/load-pdf pdfdoc)]
    (let [info (.. doc getDocumentInformation)]
      (into {}
            (map #(hash-map (str %1) (.. info (getCustomMetadataValue %1)))
                 (.. info getMetadataKeys))))))

(defn protect-doc
  "Protect the document with password."
  [pdfdoc owner-password
   & {:keys [user-password
             output-pdf
             can-assemble-document
             can-extract-content
             can-extract-for-accessibility
             can-fill-in-form
             can-modify
             can-modify-annotations
             can-print
             can-print-degraded
             read-only]
      :or {user-password ""
           can-assemble-document false
           can-extract-content false
           can-extract-for-accessibility false
           can-fill-in-form false
           can-modify false
           can-modify-annotations false
           can-print false
           can-print-degraded false
           read-only true}}]
  (with-open [doc (common/load-pdf pdfdoc)]
    (->> (doto (AccessPermission.)
           (.setCanAssembleDocument can-assemble-document)
           (.setCanExtractContent can-extract-content)
           (.setCanExtractForAccessibility can-extract-for-accessibility)
           (.setCanFillInForm can-fill-in-form)
           (.setCanModify can-modify)
           (.setCanModifyAnnotations can-modify-annotations)
           (.setCanPrint can-print)
           (.setCanPrintDegraded can-print-degraded)
           #(when read-only (.setReadOnly %)))
         (#(doto (StandardProtectionPolicy. owner-password user-password %)
             (.setEncryptionKeyLength 128)))
         (.protect doc))
    (.save doc output-pdf)))
