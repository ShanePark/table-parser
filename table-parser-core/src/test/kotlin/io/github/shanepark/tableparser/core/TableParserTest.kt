package io.github.shanepark.tableparser.core

import io.github.shanepark.tableparser.core.config.WorkbookConfig
import io.github.shanepark.tableparser.core.domain.CellProperty
import io.github.shanepark.tableparser.core.domain.enums.WorkbookColor
import org.apache.commons.io.FileUtils
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class TableParserTest {

    @Test
    fun parseHtml() {
        val workbookConfig = WorkbookConfig()
            .addCellProperty(className = "special", CellProperty(backgroundColor = WorkbookColor.DARK_GREY))
        val tableParser = TableParser(workbookConfig = workbookConfig)
        val excelResult = tableParser.htmlToExcel(sampleHTML)

        assertThat(excelResult).isNotNull
        assertThat(excelResult.size).isGreaterThan(0)
        excelResult.use { res ->
            val uerHome = System.getProperty("user.home")
            val saveFile = File("$uerHome/Downloads/test.xlsx")
            saveFile.deleteOnExit()
            FileUtils.copyFile(res.file, saveFile)
            assertThat(saveFile.exists()).isTrue

            // poi open file
            XSSFWorkbook(res.getInputStream()).use { workbook ->
                val sheet = workbook.getSheetAt(0)
                val firstRow = sheet.getRow(0)
                assertThat(firstRow.getCell(0).stringCellValue).isEqualTo("65")
                assertThat(firstRow.getCell(1).stringCellValue).isEmpty()
                assertThat(firstRow.getCell(2).stringCellValue).isEqualTo("40")
                assertThat(firstRow.getCell(3).stringCellValue).isEmpty()
                assertThat(firstRow.getCell(4).stringCellValue).isEqualTo("20")
                assertThat(firstRow.getCell(5).stringCellValue).isEmpty()
                val secondRow = sheet.getRow(1)
                secondRow.forEachIndexed { i, cell ->
                    assertThat(cell.stringCellValue).isNotEmpty
                    if (i % 2 == 0) {
                        assertThat(secondRow.getCell(i).stringCellValue).isEqualTo("Men")
                    } else {
                        assertThat(secondRow.getCell(i).stringCellValue).isEqualTo("Women")
                    }
                }
                val thirdRow = sheet.getRow(2)
                assertThat(thirdRow.getCell(0).stringCellValue).isEqualTo("82")
                assertThat(thirdRow.getCell(1).stringCellValue).isEqualTo("85")
                assertThat(thirdRow.getCell(1).cellStyle.fillForegroundColorColor).isEqualTo(WorkbookColor.DARK_GREY.xssf)
                assertThat(thirdRow.getCell(2).stringCellValue).isEqualTo("78")
                assertThat(thirdRow.getCell(3).stringCellValue).isEqualTo("82")
                assertThat(thirdRow.getCell(4).stringCellValue).isEqualTo("77")
                assertThat(thirdRow.getCell(5).stringCellValue).isEqualTo("81")
            }
        }
    }

    private val sampleHTML =
        """
            <table>
                <thead>
                  <tr> 
                    <th colspan="2">65</th> 
                    <th colspan="2">40</th> 
                    <th colspan="2">20</th> 
                  </tr> 
                  <tr> 
                    <th>Men</th> 
                    <th>Women</th> 
                    <th>Men</th>
                    <th>Women</th>
                    <th>Men</th>
                    <th>Women</th> 
                  </tr>
               </thead>
               <tbody>
                  <tr>
                   <td>82</td> 
                   <td class="special">85</td>
                   <td>78</td>
                   <td>82</td>
                   <td>77</td>
                   <td>81</td> 
                  </tr>
              </tbody>
            </table>   
            """
}
