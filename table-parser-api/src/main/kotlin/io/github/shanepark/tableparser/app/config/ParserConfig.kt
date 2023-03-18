package io.github.shanepark.tableparser.app.config

import io.github.shanepark.tableparser.core.TableParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ParserConfig {

    @Bean
    fun tableParser(): TableParser {
        return TableParser()
    }

}
