(ns pdfboxing.image-test
  (:require [clojure.test :refer [deftest is]]
            [pdfboxing.image :as image])
  (:import [java.awt.image BufferedImage]))

(deftest export-to-image
  (let [file "test/pdfs/multi-page.pdf"
        exporting-outcome (image/export-to-image :input file)
        exporting-outcome-other-page (image/export-to-image :input file :page-idx 1)
        exporting-outcome-small-dpi (image/export-to-image :input file :dpi 72)]
    (is (instance? BufferedImage exporting-outcome))
    (is (instance? BufferedImage exporting-outcome-other-page))
    (is (thrown? IllegalArgumentException (image/export-to-image :input file :page-idx 100)))
    (is (instance? BufferedImage exporting-outcome-small-dpi))
    (is (not= exporting-outcome exporting-outcome-other-page))
    (is (> (.getWidth exporting-outcome) (.getWidth exporting-outcome-small-dpi)))))
