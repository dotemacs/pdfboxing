# `pdfboxing`

Nice wrapper of [PDFBox](http://pdfbox.apache.org/) in Clojure.

## Usage

### Merge multiple PDFs

```clojure
(require '[pdfboxing.merge :as pdf])
(pdf/merge-pdfs :input ["pdfs/clojure-1.pdf" "pdfs/clojure-2.pdf"] :output "foo.pdf")
```

## License

Released under the [BSD License](http://www.opensource.org/licenses/bsd-license.php).
