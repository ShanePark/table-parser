package io.github.shanepark.tableparser.core.config

import io.github.shanepark.tableparser.core.TableParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "table-parser", name = ["enabled"], havingValue = "true")
@ConditionalOnMissingBean(TableParser::class)
open class TableParserAutoConfiguration {

    private val log: Logger = LoggerFactory.getLogger(TableParserAutoConfiguration::class.java)

    @Bean
    open fun tableParser(): TableParser {
        val workbookConfig = WorkbookConfig()
        log.info("TableParser AutoConfig enabled: $workbookConfig")
        return TableParser(workbookConfig)
    }

}
