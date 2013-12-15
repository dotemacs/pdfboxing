# `pdfboxing`

Clojure PDF manipulation library & wrapper for [PDFBox](http://pdfbox.apache.org/).

[![Continuous Integration status](https://secure.travis-ci.org/dotemacs/pdfboxing.png)](http://travis-ci.org/dotemacs/pdfboxing)


## Usage

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

To fill in form's field supply a hash map with field names and desired
values. It will create a copy of **fillable.pdf** as **new.pdf** with
the fields filled in:

```clojure
(require '[pdfboxing.form :as form])
(form/set-fields "pdfs/fillable.pdf" "pdfs/new.pdf" {"Text10" "My first name"})
```

## License

Released under the [BSD License](http://www.opensource.org/licenses/bsd-license.php).


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/dotemacs/pdfboxing/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
