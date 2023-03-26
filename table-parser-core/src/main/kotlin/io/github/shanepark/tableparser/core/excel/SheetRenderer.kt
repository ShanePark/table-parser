package io.github.shanepark.tableparser.core.excel

import io.github.shanepark.tableparser.core.config.WorkbookConfig
import io.github.shanepark.tableparser.core.domain.CellStyleVO
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class SheetRenderer(
    private val workbook: XSSFWorkbook,
    private val workbookConfig: WorkbookConfig,
    private val dataFormatMap: MutableMap<String, Short>,
    private val cellStyleMap: MutableMap<CellStyleVO, XSSFCellStyle>,
    private val sheet: Sheet,
    private val tableRows: Elements,
    private val excelRows: Array<XSSFRow>,
) {
    private val height = excelRows.size
    private val width = excelRows[0].lastCellNum.toInt()
    private val isCreatedMatrix = Array(height) { BooleanArray(width) { false } }

    fun render() {
        for (rowIndex in 0 until height) {
            val columns = tableRows[rowIndex].select("td, th")
            val excelRow = excelRows[rowIndex]
            var columnIndex = 0
            for (column in columns) {
                while (isCreatedMatrix[rowIndex][columnIndex]) {
                    columnIndex++
                }
                val cell = excelRow.getCell(columnIndex)
                cell.setCellValue(column.text())
                applyCellStyle(column, cell)
                mergeCells(rowIndex, columnIndex, column)
            }
        }
    }

    private fun mergeCells(rowIndex: Int, columnIndex: Int, column: Element) {
        val rowSpan = column.attr("rowspan").toIntOrNull() ?: 1
        val colSpan = column.attr("colspan").toIntOrNull() ?: 1
        for (i in 0 until rowSpan) {
            for (j in 0 until colSpan) {
                isCreatedMatrix[rowIndex + i][columnIndex + j] = true
            }
        }
        if (rowSpan == 1 && colSpan == 1)
            return

        val cellRangeAddress =
            CellRangeAddress(rowIndex, rowIndex + rowSpan - 1, columnIndex, columnIndex + colSpan - 1)
        sheet.addMergedRegion(cellRangeAddress)
    }

    private fun applyCellStyle(column: Element, cell: XSSFCell) {
        val cellStyleVO = CellStyleVO(
            isThead = isInThead(column),
            isNumber = isNumber(column),
            classNames = getClassNames(column)
        )

        cell.cellStyle = cellStyleMap[cellStyleVO] ?: run {
            createCellStyle(cellStyleVO, column)
        }
    }

    private fun createCellStyle(cellStyleVO: CellStyleVO, column: Element): XSSFCellStyle {
        val cellStyle = workbook.createCellStyle()
        cellStyleMap[cellStyleVO] = cellStyle

        workbookConfig.defaultProperty.applyCellStyle(workbook, cellStyle, dataFormatMap)
        if (cellStyleVO.isThead) {
            workbookConfig.tHeadProperty.applyCellStyle(workbook, cellStyle, dataFormatMap)
        }
        if (cellStyleVO.isNumber) {
            workbookConfig.numberProperty.applyCellStyle(workbook, cellStyle, dataFormatMap)
        }
        applyCustomClassStyles(column, cellStyle)

        return cellStyle
    }

    private fun getClassNames(column: Element): String {
        val classNames = column.classNames()
        column.parents().forEach { parent ->
            classNames.addAll(parent.classNames())
        }
        return classNames.sorted().joinToString(" ")
    }

    private fun applyCustomClassStyles(column: Element, cellStyle: XSSFCellStyle) {
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

}
