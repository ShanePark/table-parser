package io.github.shanepark.tableparser.core

import io.github.shanepark.tableparser.core.config.WorkbookConfig
import io.github.shanepark.tableparser.core.domain.ExcelResult
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class TableParser(
    private val workbookConfig: WorkbookConfig
) {

    fun parseHtml(html: String): ExcelResult {
        val tables = Jsoup.parse(html).select("table")
        val tableCount = tables.size
        if (tableCount == 0) {
            throw IllegalArgumentException("No table found in html")
        }

        XSSFWorkbook().use { workbook ->
            val dataFormatMap = mutableMapOf<String, Short>()
            for (i in 0 until tableCount) {
                createSheetFromTable(workbook = workbook, tables = tables, index = i, dataFormatMap = dataFormatMap)
            }
            return ExcelResult(workbook)
        }
    }

    private fun createSheetFromTable(
        workbook: XSSFWorkbook,
        tables: Elements,
        index: Int,
        dataFormatMap: MutableMap<String, Short>
    ) {
        val sheet = workbook.createSheet("Sheet${index + 1}")
        val tableRows = tables[index].select("tr")

        val maxHeight = tableRows.size
        if (maxHeight == 0) {
            throw IllegalArgumentException("No table row found in table")
        }
        val maxWidth = calcMaxWidth(tableRows)
        val isCreatedMatrix = Array(maxHeight) { BooleanArray(maxWidth) { false } }
        val excelRows = initExcelTable(sheet = sheet, maxHeight = maxHeight, maxWidth = maxWidth)

        for (rowIndex in 0 until maxHeight) {
            val row = tableRows[rowIndex]
            val columns = row.select("td, th")
            val excelRow = excelRows[rowIndex]
            var columnIndex = 0
            for (column in columns) {
                while (isCreatedMatrix[rowIndex][columnIndex]) {
                    columnIndex++
                }
                val cell = excelRow.getCell(columnIndex)
                cell.setCellValue(column.text())
                configCellStyle(workbook, column, cell, dataFormatMap)
                mergeCells(sheet, isCreatedMatrix, rowIndex, columnIndex, column)
            }
        }

    }

    private fun mergeCells(
        sheet: XSSFSheet,
        createdMatrix: Array<BooleanArray>,
        rowIndex: Int,
        columnIndex: Int,
        column: Element
    ) {
        val rowSpan = column.attr("rowspan").toIntOrNull() ?: 1
        val colSpan = column.attr("colspan").toIntOrNull() ?: 1
        for (i in 0 until rowSpan) {
            for (j in 0 until colSpan) {
                createdMatrix[rowIndex + i][columnIndex + j] = true
            }
        }
        if (rowSpan == 1 && colSpan == 1) {
            return
        }
        sheet.addMergedRegion(
            CellRangeAddress(
                rowIndex,
                rowIndex + rowSpan - 1,
                columnIndex,
                columnIndex + colSpan - 1
            )
        )
    }

    private fun configCellStyle(
        workbook: XSSFWorkbook,
        column: Element,
        cell: XSSFCell,
        dataFormatMap: MutableMap<String, Short>
    ) {
        val cellStyle = workbook.createCellStyle()

        workbookConfig.defaultProperty.applyCellStyle(workbook, cellStyle, dataFormatMap)
        if (isInThead(column)) {
            workbookConfig.tHeadProperty.applyCellStyle(workbook, cellStyle, dataFormatMap)
        }
        if (isNumber(column)) {
            workbookConfig.numberProperty.applyCellStyle(workbook, cellStyle, dataFormatMap)
        }
        applyCustomClassStyles(workbook, column, cellStyle, dataFormatMap)

        cell.cellStyle = cellStyle
    }

    private fun applyCustomClassStyles(
        workbook: XSSFWorkbook,
        column: Element,
        cellStyle: XSSFCellStyle,
        dataFormatMap: MutableMap<String, Short>
    ) {
        val customProperties = workbookConfig.cellProperties
        for (customProperty in customProperties.entries) {
            val className = customProperty.key
            if (column.hasClass(className) || column.parents().hasClass(className)) {
                customProperty.value.applyCellStyle(workbook, cellStyle, dataFormatMap)
            }
        }
    }

    private fun isNumber(column: Element): Boolean {
        return column.text() != null && column.text().matches("^[0-9,]+(\\.[0-9]+)?%?$".toRegex())
    }

    private fun isInThead(column: Element): Boolean {
        return column.parents().`is`("thead")
    }

    private fun initExcelTable(sheet: XSSFSheet, maxHeight: Int, maxWidth: Int): Array<XSSFRow> {
        val excelRows = Array(maxHeight) { sheet.createRow(it) }
        for (i in 0 until maxHeight) {
            for (j in 0 until maxWidth) {
                excelRows[i].createCell(j)
            }
        }
        return excelRows
    }

    private fun calcMaxWidth(tableRows: Elements): Int {
        val firstRow = tableRows[0]
        val columns = firstRow.select("td, th")
        var width = 0
        for (column in columns) {
            val colspan = column.attr("colspan").toIntOrNull() ?: 1
            width += colspan
        }
        return width
    }

}
