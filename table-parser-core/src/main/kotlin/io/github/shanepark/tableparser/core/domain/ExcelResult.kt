package io.github.shanepark.tableparser.core.domain

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.InputStream

class ExcelResult(
    private val workbook: XSSFWorkbook
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
