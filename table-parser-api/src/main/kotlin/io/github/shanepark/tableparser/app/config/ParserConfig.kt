package io.github.shanepark.tableparser.app.config

import io.github.shanepark.tableparser.core.TableParser
import io.github.shanepark.tableparser.core.config.TableParserAutoConfiguration
import io.github.shanepark.tableparser.core.config.WorkbookConfig
import io.github.shanepark.tableparser.core.domain.CellProperty
import io.github.shanepark.tableparser.core.domain.enums.WorkbookColor.*
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ParserConfig {
    private val log: Logger = LoggerFactory.getLogger(TableParserAutoConfiguration::class.java)

    @Bean
    fun tableParser(): TableParser {
        val defaultProperty = CellProperty(
            backgroundColor = WHITE,
            textColor = BLACK,
            alignment = HorizontalAlignment.CENTER,
            verticalAlignment = VerticalAlignment.CENTER,
            borderStyle = BorderStyle.THIN,
        )
        val tHeadProperty = CellProperty(
            backgroundColor = DARK_GREY,
            textColor = WHITE,
            alignment = HorizontalAlignment.CENTER,
            verticalAlignment = VerticalAlignment.CENTER,
            borderStyle = BorderStyle.THIN,
        )
        val numberProperty = CellProperty(
            backgroundColor = WHITE,
            textColor = BLACK,
            alignment = HorizontalAlignment.RIGHT,
            verticalAlignment = VerticalAlignment.CENTER,
            format = "#,##0;-#,##0",
        )

        val workbookConfig = WorkbookConfig(
            defaultProperty = defaultProperty,
            tHeadProperty = tHeadProperty,
            numberProperty = numberProperty,
        )

        val redConfig = CellProperty(
            backgroundColor = RED,
            textColor = WHITE,
        )

        val sumProperty = CellProperty(backgroundColor = LIGHT_GREY)
        workbookConfig.addCustomProperty("sum", sumProperty)
        workbookConfig.addCustomProperty("red", redConfig)

        log.info("Table Parser is set. workbook config: $workbookConfig")
        return TableParser(workbookConfig)
    }

}
