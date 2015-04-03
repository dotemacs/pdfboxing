# `pdfboxing`

Clojure PDF manipulation library & wrapper for [PDFBox](http://pdfbox.apache.org/).

[![Continuous Integration status](https://secure.travis-ci.org/dotemacs/pdfboxing.png)](http://travis-ci.org/dotemacs/pdfboxing)

[!["Leiningen version"](https://clojars.org/pdfboxing/latest-version.svg)](https://clojars.org/pdfboxing)

## Usage

### Extract text

```clojure
(require '[pdfboxing.text :as text])
(text/extract "test/pdfs/hello.pdf")
```

### Merge multiple PDFs

```clojure
(require '[pdfboxing.merge :as pdf])
(pdf/merge-pdfs :input ["pdfs/clojure-1.pdf" "pdfs/clojure-2.pdf"] :output "foo.pdf")
```

### List form fields of a PDF

To list fields and values:

```clojure
(require '[pdfboxing.form :as form])
(form/get-fields "pdfs/interactiveform.pdf"))
{"Emergency_Phone" "", "ZIP" "", "COLLEGE NO DEGREE" "", ...}
```
### Fill in PDF forms

To fill in form's field supply a hash map with field names and desired
values. It will create a copy of **fillable.pdf** as **new.pdf** with
the fields filled in:

```clojure
(require '[pdfboxing.form :as form])
(form/set-fields "pdfs/fillable.pdf" "pdfs/new.pdf" {"Text10" "My first name"})
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

## License

Released under the [BSD License](http://www.opensource.org/licenses/bsd-license.php).


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/dotemacs/pdfboxing/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
