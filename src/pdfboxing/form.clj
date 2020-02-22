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
  "Take `input` PDF, a map `new-fields` which will set the values of the
  fields and create `output` PDF.

  For example:

  {\"foo\" \"bar\"}

  will set the value of \"bar\" to the field \"foo\".

  If the field \"foo\" has children, then all its children will get
  the value of \"bar\"."
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
  "Take an `input` PDF, a string `output` as the new file to be created
  and a map `fields-map` with keys as the current form field names and
  values as the new names, and rename them.

  e.g. (rename-fields \"input.pdf\" \"output.pdf\" {\"foo\" \"bar\"})
  will create a new PDF document output.pdf with a field foo renamed
  to bar.

  If the field to be renamed is nested, only partial name of the field
  is renamed. For example in this `fields-map`:

  {\"foo\" {\"foo.0\" \"bar\"}}

  the result will be that foo.0 is now renamed to foo.bar.

  But if you were to rename foo into bar, where foo is the top level
  field name, then all the children of the foo would also be
  renamed. For this `fields-map`:

  {\"foo\" \"bar\"}

  and if the foo field looked like:

  {\"foo\" {\"foo.0\" \"x\" \"foo.1\" \"y\"}}

  the results would be:

  {\"bar\" {\"bar.0\" \"x\" \"bar.1\" \"y\"}}."
  [input output fields-map]
  (with-open [doc (common/obtain-document input)]
    (let [form (common/get-form doc)]
      (doseq [field fields-map]
        (-> (.getField form (str (first field)))
            (.setPartialName (str (last field)))))
      (.save doc output))))
