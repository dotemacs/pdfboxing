(ns pdfboxing.security
  (:require [pdfboxing.common :as common])
  (:import [org.apache.pdfbox.pdmodel.encryption
            AccessPermission StandardProtectionPolicy]))

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
           can-extract-for-accessibility true
           can-fill-in-form true
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
