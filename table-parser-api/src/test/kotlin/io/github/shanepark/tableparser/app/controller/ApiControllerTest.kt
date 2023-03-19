package io.github.shanepark.tableparser.app.controller

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import java.io.File

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiControllerTest {
    @Autowired
    lateinit var restClient: TestRestTemplate

    @Test
    fun apiTest() {
        val entity = restClient.postForEntity("/excel", sampleHTML, ByteArray::class.java)

        val tempFile = File.createTempFile("result", ".xlsx")
        tempFile.writeBytes(entity.body!!)

        XSSFWorkbook(tempFile).use { workbook ->
            val sheet = workbook.getSheetAt(0)
            val firstRow = sheet.getRow(0)
            Assertions.assertThat(firstRow.getCell(0).stringCellValue).isEqualTo("65")
            Assertions.assertThat(firstRow.getCell(1).stringCellValue).isEmpty()
            Assertions.assertThat(firstRow.getCell(2).stringCellValue).isEqualTo("40")
            Assertions.assertThat(firstRow.getCell(3).stringCellValue).isEmpty()
            Assertions.assertThat(firstRow.getCell(4).stringCellValue).isEqualTo("20")
            Assertions.assertThat(firstRow.getCell(5).stringCellValue).isEmpty()
            val secondRow = sheet.getRow(1)
            secondRow.forEachIndexed { i, cell ->
                Assertions.assertThat(cell.stringCellValue).isNotEmpty
                if (i % 2 == 0) {
                    Assertions.assertThat(secondRow.getCell(i).stringCellValue).isEqualTo("Men")
                } else {
                    Assertions.assertThat(secondRow.getCell(i).stringCellValue).isEqualTo("Women")
                }
            }
            val thirdRow = sheet.getRow(2)
            Assertions.assertThat(thirdRow.getCell(0).stringCellValue).isEqualTo("82")
            Assertions.assertThat(thirdRow.getCell(1).stringCellValue).isEqualTo("85")
            Assertions.assertThat(thirdRow.getCell(2).stringCellValue).isEqualTo("78")
            Assertions.assertThat(thirdRow.getCell(3).stringCellValue).isEqualTo("82")
            Assertions.assertThat(thirdRow.getCell(4).stringCellValue).isEqualTo("77")
            Assertions.assertThat(thirdRow.getCell(5).stringCellValue).isEqualTo("81")
        }

        tempFile.deleteOnExit()
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
                   <td>85</td>
                   <td>78</td>
                   <td>82</td>
                   <td>77</td>
                   <td>81</td> 
                  </tr>
              </tbody>
            </table>   
            """

}

