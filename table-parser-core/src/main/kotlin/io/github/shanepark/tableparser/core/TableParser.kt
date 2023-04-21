package io.github.shanepark.tableparser.core

import io.github.shanepark.tableparser.core.config.WorkbookConfig
import io.github.shanepark.tableparser.core.domain.ExcelResult
import io.github.shanepark.tableparser.core.excel.ExcelRenderer
import org.jsoup.Jsoup

class TableParser(
    private val workbookConfig: WorkbookConfig
) {

    fun htmlToExcel(html: String): ExcelResult {
        val tables = Jsoup.parse(html).select("table")
        val tableCount = tables.size
        if (tableCount == 0) {
            throw IllegalArgumentException("No table found in html")
        }

        val excelRenderer = ExcelRenderer(tables, workbookConfig)

        try {
            excelRenderer.createWorkbook().use { workbook ->
                return ExcelResult(workbook)
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
            throw IllegalArgumentException("Invalid table structure")
        }
    }

}
