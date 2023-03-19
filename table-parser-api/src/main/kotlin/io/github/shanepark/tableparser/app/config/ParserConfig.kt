package io.github.shanepark.tableparser.app.config

import io.github.shanepark.tableparser.core.TableParser
import io.github.shanepark.tableparser.core.config.WorkbookConfig
import io.github.shanepark.tableparser.core.domain.CellProperty
import io.github.shanepark.tableparser.core.domain.enums.WorkbookColor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ParserConfig {

    @Bean
    fun tableParser(): TableParser {
        val tHeadProperty = CellProperty(backgroundColor = WorkbookColor.DARK_GREY, textColor = WorkbookColor.BLACK)
        val specialProp = CellProperty(backgroundColor = WorkbookColor.BLACK, textColor = WorkbookColor.WHITE)

        val workbookConfig = WorkbookConfig(tHeadProperty = tHeadProperty)
            .addCellProperty("special", specialProp)
        return TableParser(workbookConfig)
    }

}
