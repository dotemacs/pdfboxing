# `pdfboxing`

Nice wrapper of [PDFBox](http://pdfbox.apache.org/) in Clojure.

[![Continuous Integration status](https://secure.travis-ci.org/dotemacs/pdfboxing.png)](http://travis-ci.org/dotemacs/pdfboxing)


## Usage

### Merge multiple PDFs

```clojure
(require '[pdfboxing.merge :as pdf])
(pdf/merge-pdfs :input ["pdfs/clojure-1.pdf" "pdfs/clojure-2.pdf"] :output "foo.pdf")
```

### List form fields of a PDF

To get just the names of fields:

```clojure
(require '[pdfboxing.form :as form])
(form/get-fields "pdfs/interactiveform.pdf")
=> ["HIGH SCHOOL DIPLOMA" "TRADE CERTIFICATE" "COLLEGE NO DEGREE" ... ]
```

To list fields and values:

```clojure
(require '[pdfboxing.form :as form])
(form/get-fields-and-values "pdfs/interactiveform.pdf"))
{"Emergency_Phone" "", "ZIP" "", "COLLEGE NO DEGREE" "", ...}
```

## License

Released under the [BSD License](http://www.opensource.org/licenses/bsd-license.php).
