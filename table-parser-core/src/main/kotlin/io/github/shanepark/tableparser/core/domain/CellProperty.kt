package io.github.shanepark.tableparser.core.domain

import io.github.shanepark.tableparser.core.domain.enums.WorkbookColor
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class CellProperty(
    private val alignment: HorizontalAlignment = HorizontalAlignment.CENTER,
    private val verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER,
    private val borderStyle: BorderStyle = BorderStyle.THIN,
    private val backgroundColor: WorkbookColor? = null,
    private val textColor: WorkbookColor? = null,
    private val format: String? = null,
) {

    fun applyCellStyle(workbook: XSSFWorkbook, cellStyle: XSSFCellStyle, dataFormatMap: MutableMap<String, Short>) {
        cellStyle.alignment = alignment
        cellStyle.verticalAlignment = verticalAlignment
        cellStyle.borderBottom = borderStyle
        cellStyle.borderLeft = borderStyle
        cellStyle.borderRight = borderStyle
        cellStyle.borderTop = borderStyle

        backgroundColor?.let {
            if (backgroundColor != WorkbookColor.WHITE) {
                cellStyle.setFillBackgroundColor(backgroundColor.xssf)
            }
            cellStyle.setFillForegroundColor(backgroundColor.xssf)
            cellStyle.fillPattern = FillPatternType.SOLID_FOREGROUND
        }

        textColor?.let {
            val font = workbook.createFont()
            font.setColor(textColor.xssf)
            cellStyle.setFont(font)
        }

        format?.let {
            var index = dataFormatMap[format]
            if (index == null) {
                val dataformat = workbook.createDataFormat()
                index = dataFormatMap.size.toShort()
                dataFormatMap[format] = index
                dataformat.putFormat(index, format)
            }
            cellStyle.dataFormat = index
        }

    }

    override fun toString(): String {
        return "CellProperty(alignment=$alignment, verticalAlignment=$verticalAlignment, backgroundColor=$backgroundColor, textColor=$textColor, format=$format)"
    }


}
