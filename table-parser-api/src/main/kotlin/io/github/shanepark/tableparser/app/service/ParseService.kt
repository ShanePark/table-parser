package io.github.shanepark.tableparser.app.service

import io.github.shanepark.tableparser.core.TableParser
import io.github.shanepark.tableparser.core.domain.ExcelResult
import org.springframework.stereotype.Service

@Service
class ParseService(
    private val tableParser: TableParser
) {

    fun parseHtml(html: String): ExcelResult {
        return tableParser.parseHtml(html)
    }

}
