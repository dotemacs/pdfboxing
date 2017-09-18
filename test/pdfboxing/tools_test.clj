(ns pdfboxing.tools-test
  (:require [clojure.test :refer :all]
            [pdfboxing.tools :refer :all]))

(def line-separator (System/getProperty "line.separator"))

(deftest pdf-to-html-tool
  (let [html (pdf-to-html "test/pdfs/hello.pdf")]
    (is (= (str "
<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"
\"http://www.w3.org/TR/html4/loose.dtd\">
<html><head><title></title>
<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"UTF-8\">
                </head>
                <body>
                <div style=\"page-break-before:always; page-break-after:always\"><div><p>Hello, this is pdfboxing.text</p>
                </div></div>
                </body></html>" line-separator)) (slurp "test/pdfs/hello.html"))))
