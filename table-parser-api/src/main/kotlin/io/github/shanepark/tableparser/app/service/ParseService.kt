package io.github.shanepark.tableparser.app.service

import io.github.shanepark.tableparser.core.TableParser
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ParseService(
    private val tableParser: TableParser
) {

    fun parseHtml(html: String): ResponseEntity<InputStreamResource> {
        val excelResult = tableParser.htmlToExcel(html)
        return ResponseEntity.ok()
            .contentLength(excelResult.size)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "attachment; filename=\"result.xlsx\"")
            .body(InputStreamResource(excelResult.getInputStream()))
    }

}
