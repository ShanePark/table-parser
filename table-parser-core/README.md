Table-Parser-Core
================

## Introduction

if you don't need Web, but only need to parse table, you can use this core.  
I recommend you to handle IllegalArgumentException, because it will throw when you parse a table with wrong format.

## Usage

1. Add the dependency to your project

    ```groovy
    implementation(project(":table-parser-core"))
    ```

2. Register TableParser  
   if you want to use default parser, just don't register. Spring Boot Auto Configuration will register it for you.  
   Otherwise, you can register it on your taste.

    ```kotlin

    @Configuration
    class ParserConfig {
   
       @Bean
       fun tableParser(): TableParser {
           val defaultProperty = CellProperty()
           val tHeadProperty = CellProperty()
           val numberProperty = CellProperty()
   
           val workbookConfig = WorkbookConfig(
               defaultProperty = defaultProperty,
               tHeadProperty = tHeadProperty,
               numberProperty = numberProperty,
           )
   
           return TableParser(workbookConfig)
       }
   
    }
    ``` 
