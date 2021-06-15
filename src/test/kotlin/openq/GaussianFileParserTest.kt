package openq

import openq.vasp.GaussianFileParser
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException

open class GaussianFileParserTest {

    private val gaussianLogFile = File("/Users/huobingnan/code/kotlin/gemovasp/src/test/resources/NNNN-Cu1-2H-2PhCC-n.log")

    @Before
    fun testFileValidation() {
        if (!gaussianLogFile.exists())
            throw FileNotFoundException("Test file not found!!!")
    }


    @Test
    fun testParseInputFile() {
        GaussianFileParser.parse(gaussianLogFile)
    }

    @Test
    fun testParse() {
        val result = GaussianFileParser.parse(gaussianLogFile)
        println(result)
        result.lastAtomCoordinateList.forEach {
            println(it)
        }
    }
}