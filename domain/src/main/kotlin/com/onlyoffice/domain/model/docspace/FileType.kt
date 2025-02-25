package com.onlyoffice.domain.model.docspace

import kotlinx.serialization.Serializable

@Serializable
sealed class FileType(val ext: String?) {
    class Spreadsheet(ext: String?) : FileType(ext)
    class Presentation(ext: String?) : FileType(ext)
    class Document(ext: String?) : FileType(ext)
    class Pdf(ext: String?) : FileType(ext)
}