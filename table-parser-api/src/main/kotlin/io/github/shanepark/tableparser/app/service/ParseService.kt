package io.github.shanepark.tableparser.app.service

import io.github.shanepark.tableparser.core.TableParser
import org.springframework.stereotype.Service

@Service
class ParseService(
    private val tableParser: TableParser
) {

    fun parse(html: String): Any {
        return tableParser.parseHtml(html)
    }

}
