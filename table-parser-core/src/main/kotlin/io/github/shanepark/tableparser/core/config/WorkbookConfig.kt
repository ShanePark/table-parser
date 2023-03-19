package io.github.shanepark.tableparser.core.config

import io.github.shanepark.tableparser.core.domain.CellProperty
import io.github.shanepark.tableparser.core.domain.enums.WorkbookColor
import org.apache.poi.ss.usermodel.HorizontalAlignment

class WorkbookConfig(
    val defaultProperty: CellProperty = CellProperty(),
    val tHeadProperty: CellProperty = tHeadProp,
    val numberProperty: CellProperty = numberProp,
) {
    val cellProperties: MutableMap<String, CellProperty> = mutableMapOf()
    fun addCellProperty(className: String, cellProperty: CellProperty): WorkbookConfig {
        if (cellProperties.containsKey(className)) {
            throw IllegalArgumentException("CellProperty for $className already exists")
        }
        cellProperties[className] = cellProperty
        return this
    }

    companion object {
        val tHeadProp = CellProperty(
            backgroundColor = WorkbookColor.BLACK,
            textColor = WorkbookColor.WHITE,
        )
        val numberProp = CellProperty(
            alignment = HorizontalAlignment.RIGHT,
            format = "#,##0;-#,##0"
        )
    }
}
