package io.github.shanepark.tableparser.core.domain

import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.InputStream

class ExcelResult(
    private val workbook: Workbook
) : AutoCloseable {

    val file: File = File.createTempFile("tmp", ".xlsx")
    val size: Long

    init {
        this.workbook.write(file.outputStream())
        this.size = file.length()
    }

    fun getInputStream(): InputStream = file.toURI().toURL().openStream()

    override fun close() {
        if (file.exists()) {
            file.delete()
        }
    }
}
