package io.github.shanepark.tableparser.app.controller

import io.github.shanepark.tableparser.app.service.ParseService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiController(
    val parseService: ParseService
) {

    @GetMapping("/")
    fun parseHtml(): ResponseEntity<String> {
        val parse = parseService.parse("html")
        return ResponseEntity.ok(parse.toString())
    }
}
