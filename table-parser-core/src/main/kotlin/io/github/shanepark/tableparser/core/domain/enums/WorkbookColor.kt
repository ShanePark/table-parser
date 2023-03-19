package io.github.shanepark.tableparser.core.domain.enums

import org.apache.poi.xssf.usermodel.XSSFColor

enum class WorkbookColor(
    val xssf: XSSFColor
) {
    DARK_GREY(XSSFColor(byteArrayOf(0x66.toByte(), 0x66.toByte(), 0x66.toByte()))),
    LIGHT_GREY(XSSFColor(byteArrayOf(0xCC.toByte(), 0xCC.toByte(), 0xCC.toByte()))),
    WHITE(XSSFColor(byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()))),
    BLACK(XSSFColor(byteArrayOf(0x00.toByte(), 0x00.toByte(), 0x00.toByte()))),
    RED(XSSFColor(byteArrayOf(0xFF.toByte(), 0x00.toByte(), 0x00.toByte()))),
}
