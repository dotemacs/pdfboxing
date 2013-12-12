# `pdfboxing`

Nice wrapper of [PDFBox](http://pdfbox.apache.org/) in Clojure.

## Usage

### Merge multiple PDFs

```clojure
(require '[pdfboxing.merge :as pdf])
(pdf/merge-pdfs "output.pdf" "a.pdf" "b.pdf" "c.pdf")
```

## License

Released under the [BSD License](http://www.opensource.org/licenses/bsd-license.php).
