package io.github.shanepark.tableparser.app.controller

import io.github.shanepark.tableparser.app.service.ParseService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(
    val parseService: ParseService
) {

    @PostMapping("/excel")
    fun parseHtml(@RequestBody html: String): ResponseEntity<InputStreamResource> {
        return parseService.parseHtml(html)
    }
}
