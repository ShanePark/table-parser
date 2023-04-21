package io.github.shanepark.tableparser.core.config

import io.github.shanepark.tableparser.core.domain.CellProperty
import io.github.shanepark.tableparser.core.domain.enums.WorkbookColor
import org.apache.poi.ss.usermodel.HorizontalAlignment
import java.util.*

class WorkbookConfig(
    val defaultProperty: CellProperty = CellProperty(),
    val tHeadProperty: CellProperty = tHeadProp,
    val numberProperty: CellProperty = numberProp,
) {
    private val _customProperties: MutableMap<String, CellProperty> = mutableMapOf()
    val customProperties: Map<String, CellProperty>
        get() = Collections.unmodifiableMap(_customProperties)

    fun addCustomProperty(className: String, cellProperty: CellProperty): WorkbookConfig {
        if (_customProperties.containsKey(className)) {
            throw IllegalArgumentException("CellProperty for $className already exists")
        }
        _customProperties[className] = cellProperty
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

    override fun toString(): String {
        return "\n* WorkbookConfig *" +
                "\n - defaultProperty= $defaultProperty, " +
                "\n - tHeadProperty= $tHeadProperty, " +
                "\n - numberProperty= $numberProperty, " +
                "\n - customProperties= ${customPropertiesToString()}"
    }

    private fun customPropertiesToString(): String {
        if (customProperties.isEmpty()) return "(empty)"
        return customProperties.map {
            "\n\t - ${it.key} = ${it.value}"
        }.joinToString()
    }

}
