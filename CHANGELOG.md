# Changelog

## 0.1.15.4-SNAPSHOT

### Changed
- Updated the CI jobs, by adding more JVMs to test against & added a clj-kondo lint job[#70](https://github.com/dotemacs/pdfboxing/pull/70)
- `pdfboxing.split/split-pdf` wouldn't close a document when splitting[#73](https://github.com/dotemacs/pdfboxing/pull/73)
- Updated CI depdendency, GitHub action `actions/cache` to v3.3.1[#75](https://github.com/dotemacs/pdfboxing/pull/75)
- Updated PDFBox to 3.0.1 [#79](https://github.com/dotemacs/pdfboxing/pull/79)
- Updated `actions/checkout` to v4 [ecd8fdd0a113920677d5dd4689ce4de36f933fa0](https://github.com/dotemacs/pdfboxing/commit/ecd8fdd0a113920677d5dd4689ce4de36f933fa0)

### Added
- Added GitHub action based on antq, for outdated dependencies & updated outdated dependencies[#72](https://github.com/dotemacs/pdfboxing/pull/72)
- Added the licence file[#74](https://github.com/dotemacs/pdfboxing/pull/74)

### Removed
- Removed TravisCI config[#71](https://github.com/dotemacs/pdfboxing/pull/71)

## 0.1.15.3-SNAPSHOT

### Added
- Added the ability to create HTML documents from PDFs [#69](https://github.com/dotemacs/pdfboxing/pull/69)

## 0.1.15.2-SNAPSHOT

### Added
- Added the ability to merge multiple images into a single PDF[#45](https://github.com/dotemacs/pdfboxing/pull/45)
- Added the ability to load PDFs from byte arrays [#58](https://github.com/dotemacs/pdfboxing/pull/58)
- Added the ability to run tests automatically using GitHub actions [#64](https://github.com/dotemacs/pdfboxing/pull/64)

### Changed
- Upgraded `PDFBox` to `2.0.27` & `Clojure` to `1.11.1` [#68](https://github.com/dotemacs/pdfboxing/pull/68)
- Using lists for :imports [157706](https://github.com/dotemacs/pdfboxing/commit/1577064f72e34523245454bca0f232da6a3e7c2f)
- Updated documentation of `split-pdf-at` in the README [78f9](https://github.com/dotemacs/pdfboxing/commit/78f9e822a6463c84bb8f257c8bc5956bd8269258)
- Imports in `pdfboxing.common` comply with clj-kondo linting [8924eb3](https://github.com/dotemacs/pdfboxing/commit/8924eb37669d2ba22b543179c2f1d2dbfab93926)
- Made `pdfboxing.form/set-fields` more efficient [#64](https://github.com/dotemacs/pdfboxing/pull/64)
- Made `pdfboxing.form/get-fields` more robust by handling nested fields [#65](https://github.com/dotemacs/pdfboxing/pull/65)
- Made `pdfboxing.merge/merge-pdfs` more robust by allowing more input and output types [#67](https://github.com/dotemacs/pdfboxing/pull/67)
edt

## 0.1.15.1-SNAPSHOT

### Added
- Form flattening [10d4](https://github.com/dotemacs/pdfboxing/commit/10d4962209f92290b66a709a8e64edf3203eab2a)
- Upgraded `PDFBox` to `2.0.21`
- Upgraded `Clojure` to `1.10.1`
- Fix for draw-line [#51](https://github.com/dotemacs/pdfboxing/pull/51)

### Changed
- Fixed all warnings as per clj-kondo [f7e4](https://github.com/dotemacs/pdfboxing/commit/f7e4eaf7c192ab6b3db2bd8420d5011a8837cc0a)

## 0.1.15-SNAPSHOT

### Changed
- Enhanced `pdfboxing.form/set-fields` so that even sets the nested/child fields [#53](https://github.com/dotemacs/pdfboxing/pull/53)
- Enhanced `pdfboxing.form/get-fields` so that it gets even the child fields [8062](https://github.com/dotemacs/pdfboxing/commit/8062677d51e279496951f5f3630b947227150410)
- Upgraded `PDFBox` to `2.1.18`
