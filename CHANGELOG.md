# Changelog

## WIP

### Added
- Added the ability to merge multiple images into a single PDF
- Added the ability to load PDFs from byte arrays
- Added the ability to partially parse PDF content based on a vector of regions

### Changed
- Using lists for :imports
- Updated documentation of `split-pdf-at` in the README
- Imports in `pdfboxing.common` comply with clj-kondo linting

## 0.1.15.1-SNAPSHOT

### Added
- Form flattening
- Upgraded `PDFBox` to `2.0.21`
- Upgraded `Clojure` to `1.10.1`
- Fix for draw-line

### Changed
- Fixed all warnings as per clj-kondo

## 0.1.15-SNAPSHOT

### Changed

- Enhanced `pdfboxing.form/get-fields` so that it gets even the child fields
- Upgraded `PDFBox` to `2.1.18`
