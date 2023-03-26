package io.github.shanepark.tableparser.core.excel

import io.github.shanepark.tableparser.core.config.WorkbookConfig
import io.github.shanepark.tableparser.core.domain.CellStyleVO
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jsoup.select.Elements
import java.util.stream.IntStream

class ExcelRenderer(
    private val tables: Elements,
    private val workbookConfig: WorkbookConfig
) {
    private val workbook = XSSFWorkbook()
    private val dataFormatMap = mutableMapOf<String, Short>()
    private val cellStyleMap = mutableMapOf<CellStyleVO, XSSFCellStyle>()

    fun createWorkbook(): Workbook {
        IntStream.range(0, tables.size).forEach { i -> createSheet(i) }
        return workbook
    }

    private fun createSheet(index: Int) {
        val sheet = workbook.createSheet("Sheet${index + 1}")
        val tableRows = tables[index].select("tr")
        val excelRows = initExcelRows(sheet = sheet, maxHeight = tableRows.size, maxWidth = calcMaxWidth(tableRows))
        val sheetRenderer = SheetRenderer(
            workbook = workbook,
            workbookConfig = workbookConfig,
            dataFormatMap = dataFormatMap,
            cellStyleMap = cellStyleMap,
            sheet = sheet,
            tableRows = tableRows,
            excelRows = excelRows,
        )
        sheetRenderer.render()
    }

    private fun calcMaxWidth(tableRows: Elements): Int {
        if (tableRows.isEmpty())
            return 0
        val firstRow = tableRows[0]
        val columns = firstRow.select("td, th")
        var width = 0
        for (column in columns) {
            val colspan = column.attr("colspan").toIntOrNull() ?: 1
            width += colspan
        }
        return width
    }

    private fun initExcelRows(sheet: XSSFSheet, maxHeight: Int, maxWidth: Int): Array<XSSFRow> {
        val excelRows = Array(maxHeight) { sheet.createRow(it) }
        for (i in 0 until maxHeight) {
            for (j in 0 until maxWidth) {
                excelRows[i].createCell(j)
            }
        }
        return excelRows
    }

}
