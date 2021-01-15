(ns pdfboxing.overlay
  (:import
    (org.apache.pdfbox.multipdf Overlay)))

(defn overlay-pdf
  "overlay one PDF over another"
  [{:keys
    [input-file-path
     input-pdf
     overlay-file-path
     overlay-pdf
     first-page-overlay-pdf
     first-page-overlay-file-path
     last-page-overlay-file-path
     last-page-overlay-pdf
     all-page-overlay-file-path
     all-page-overlay-pdf
     odd-page-overlay-file-path
     odd-page-overlay-pdf
     even-page-overlay-file-path
     even-page-overlay-pdf
     ^String output-file-path]}]

  (let [overlay (Overlay.)]
    (.setInputFile overlay input-file-path)
    (.setInputPDF overlay input-pdf)

    (.setDefaultOverlayFile overlay overlay-file-path)
    (.setDefaultOverlayPDF overlay overlay-pdf)

    (.setFirstPageOverlayFile overlay first-page-overlay-file-path)
    (.setFirstPageOverlayPDF overlay first-page-overlay-pdf)

    (.setLastPageOverlayFile overlay last-page-overlay-file-path)
    (.setLastPageOverlayPDF overlay last-page-overlay-pdf)

    (.setAllPagesOverlayFile overlay all-page-overlay-file-path)
    (.setAllPagesOverlayPDF overlay all-page-overlay-pdf)

    (.setOddPageOverlayFile overlay odd-page-overlay-file-path)
    (.setOddPageOverlayPDF overlay odd-page-overlay-pdf)

    (.setEvenPageOverlayFile overlay even-page-overlay-file-path)
    (.setEvenPageOverlayPDF overlay even-page-overlay-pdf)

    (-> overlay (.overlay {}) (.save output-file-path))
    (.close overlay)))