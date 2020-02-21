(ns pdfboxing.form
  (:require [pdfboxing.common :as common])
  (:import (org.apache.pdfbox.pdmodel.interactive.form PDNonTerminalField)))


(defn extract-fields
  "From a given `field`, extract its name and value or if it has
  children, all the children."
  [field]
  (if (= PDNonTerminalField (type field))
    (hash-map (.getFullyQualifiedName field)
              (->> field
                   .getChildren
                   (map (fn [child]
                          (if (= PDNonTerminalField (type child))
                            (map #(extract-fields %) child)
                            (hash-map (.getFullyQualifiedName child)
                                      (.getValue child)))))
                   (into {})))
    (hash-map (.getFullyQualifiedName field) (str (.getValue field)))))


(defn get-fields
  "get all the field names and their values from a PDF document"
  [pdfdoc]
  (with-open [doc (common/obtain-document pdfdoc)]
    (->> doc
         common/get-form
         .getFields
         (map #(extract-fields %))
         (into {}))))

(defn set-fields
  "fill in the fields with the values provided"
  [input output new-fields]
  (with-open [doc (common/obtain-document input)]
    (let [form (common/get-form doc)]
      (doseq [field new-fields]
        (try
          (-> (.getField form (name (key field)))
              (.setValue (val field)))
          (catch NullPointerException e
            (throw (IllegalArgumentException. (str "Non-existing field " (key field) " provided")))))
        (.save doc output)))))

(defn rename-fields
  "take an input PDF, a new file to be created and a map with keys as
  the current form field names and values as the new names, and rename
  them"
  [input output fields-map]
  (with-open [doc (common/obtain-document input)]
    (let [form (common/get-form doc)]
      (doseq [field fields-map]
        (-> (.getField form (str (first field)))
            (.setPartialName (str (last field)))))
      (.save doc output))))
