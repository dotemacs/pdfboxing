(ns pdfboxing.form
  (:require [pdfboxing.common :as common]))

(defn get-fields
  "get all the field names and their values from a PDF document"
  [pdfdoc]
  (with-open [doc (common/obtain-document pdfdoc)]
    (->> doc
         common/get-form
         .getFields
         (map #(hash-map (.getPartialName %) (str (.getValue %))))
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
