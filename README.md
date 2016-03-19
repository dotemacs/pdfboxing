# `pdfboxing`

Clojure PDF manipulation library & wrapper for [PDFBox](http://pdfbox.apache.org/).

* [!["Clojar version"](https://img.shields.io/clojars/v/pdfboxing.svg?style=flat-square)](https://clojars.org/pdfboxing)
* [!["Continuous Integration status"](https://img.shields.io/travis/dotemacs/pdfboxing.svg?style=flat-square)](http://travis-ci.org/dotemacs/pdfboxing)
* [![License](http://img.shields.io/badge/license-BSD-brightgreen.svg?style=flat-square)](http://www.opensource.org/licenses/bsd-license)
* [![Dependency Status](https://www.versioneye.com/user/projects/5520e2a9971f7847ca0004a1/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5520e2a9971f7847ca0004a1)

## Usage

### Extract text

```clojure
(require '[pdfboxing.text :as text])
(text/extract "test/pdfs/hello.pdf")
```

### Merge multiple PDFs

```clojure
(require '[pdfboxing.merge :as pdf])
(pdf/merge-pdfs :input ["test/pdfs/clojure-1.pdf" "test/pdfs/clojure-2.pdf"] :output "foo.pdf")
```

### Split a PDF into mutliple PDDocuments
```clojure
 (require '[pdfboxing.split :as pdf])
```
List of PDDocument pages 1 through 8
```clojure
 (pdf/split-pdf :input "test/pdfs/multi-page.pdf" :start 1 :end 8)
```
Splits the PDF into single pages as a list of PDDocument
```clojure
 (pdf/split-pdf :input "test/pdfs/multi-page.pdf")
```
Splits the PDF in half and writes them to disk as clojure-1.pdf and clojure-2.pdf
```clojure
 (pdf/split-pdf-at :input "test/pdfs/multi-page.pdf")
```
Splits into two PDFs, the first having 5 pages and second has rest
```clojure
 (pdf/split-pdf-at :input "test/pdfs/multi-page.pdf" :split 5)
```

### List form fields of a PDF

To list fields and values:

```clojure
(require '[pdfboxing.form :as form])
(form/get-fields "test/pdfs/interactiveform.pdf"))
{"Emergency_Phone" "", "ZIP" "", "COLLEGE NO DEGREE" "", ...}
```
### Fill in PDF forms

To fill in form's field supply a hash map with field names and desired
values. It will create a copy of **fillable.pdf** as **new.pdf** with
the fields filled in:

```clojure
(require '[pdfboxing.form :as form])
(form/set-fields "test/pdfs/fillable.pdf" "test/pdfs/new.pdf" {"Text10" "My first name"})
```

### Rename form fields of a PDF

To rename PDF form fields, supply a hash map where the keys are the
current names and the values new names:

```clojure
(require '[pdfboxing.form :as form])
(form/rename-fields "test/pdfs/interactiveform.pdf" "test/pdfs/addr1.pdf" {"Address_1" "NewAddr"})
```
### Get page count of a PDF document

```clojure
(require '[pdfboxing.info :as info])
(info/page-number "test/pdfs/interactiveform.pdf")
```
### Get info about a PDF document

Such as title, author, subject, keywords, creator & producer

```clojure
(require '[pdfboxing.info :as info])
(info/about-doc "test/pdfs/interactiveform.pdf")
```

### Draw lines on a PDF document

Supply a PDF document, a name for the output PDF document, the
coordinates where the line should be drawn along with the page number
on which the line should be drawn

```clojure
(require '[pdfboxing.draw :as draw])
(draw/draw-line :input-pdf "test/pdfs/clojure-1.pdf"
                :output-pdf "ninja.pdf"
                :coordinates {:page-number 0
                              :x 0
                              :y 160
                              :x1 650
                              :y1 160})
```

## Compatibility with Pdfbox PDDocuments
The following functions referenced above have direct compatibility with Pdfbox's internal PDDocument type:
- `text/extract`
- `pdf/split-pdf`
- `form/get-fields`
- `form/set-fields`
- `form/rename-fields`
- `info/page-number`
- `draw/draw-line`

This allows you to substitute each filepath (of each function's input) referenced above with a PDDocument type.  This is helpful for example in the case that you were to want to split a PDF up by pages and then extract the text from *only* the 3rd page:
```clojure
(require '[pdfboxing.text :as text])
(require '[pdfboxing.split :as split])
(text/extract
  (nth 
    (split/split-pdf :input "test/pdfs/multi-page.pdf")
    2))
```
