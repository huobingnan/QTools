package openq.vasp
import openq.constants.CoordinateType
import openq.model.Contcar
import openq.util.Strings
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.text.ParseException

class ContcarFileParser {

    companion object {
        private val parseName = {line: String, contcar: Contcar -> contcar.name = line.trim()}

        private val parseScale = {line: String, contcar: Contcar -> contcar.scale = line.toDouble()}

        private val parseVectorA = {line: String, contcar: Contcar ->
            val vectorA = Strings.splitStringByAnyBlanks(line)
            // 初始化矩阵
            contcar.matrix = Array(3) {Array(3){0.0} }
            for (i in vectorA.indices) {
                contcar.matrix!![0][i] = vectorA[i].toDouble()
            }
        }

        // 解析晶胞向量 b
        private val parseVectorB = {line: String, contcar: Contcar ->
            val vectorB =  Strings.splitStringByAnyBlanks(line)
            for (i in vectorB.indices) {
                contcar.matrix!![1][i] = vectorB[i].toDouble()
            }

        }

        // 解析晶胞向量 c
        private val parseVectorC = {line: String, contcar: Contcar ->
            val vectorC =  Strings.splitStringByAnyBlanks(line)
            for (i in vectorC.indices) {
                contcar.matrix!![2][i] = vectorC[i].toDouble()
            }
        }

        // 解析化学体系中的组分
        private val parseComponents = {line: String, contcar: Contcar ->
            contcar.componentsNameList =  Strings.splitStringByAnyBlanks(line)
        }

        // 解析化学体系中各个组分的数目
        private val parseComponentsNumber = {line: String, contcar: Contcar ->
            contcar.componentsNumberList =  Strings.splitStringByAnyBlanks(line).map { it.toInt() }
        }

        // 解析晶体的坐标类型
        private val parseCoordinateType = {line: String, contcar: Contcar ->
            if (line.toUpperCase() == "DIRECT") {
                contcar.coordinateType = CoordinateType.Direct
            } else {
                // 注意，非Direct类型的内容，都会按照笛卡尔坐标去解析
                contcar.coordinateType = CoordinateType.Cartesian
            }
        }

        private val sum = {numbers: List<Int> ->
            var result = 0
            for (number in numbers) result += number
            result
        }

        // 解析化学体系中各个原子的坐标信息
        private val parseComponentsCoordinate = {line: String, contcar: Contcar ->
            val total = sum(contcar.componentsNumberList!!)
            if (contcar.componentAmount < total) {
                val coordinate =  Strings.splitStringByAnyBlanks(line)
                val coordinateDoubleArray = coordinate.map { it.toDouble() }.toTypedArray()
                contcar.componentsCoordinate.add(coordinateDoubleArray)
                contcar.componentAmount++
            }
        }

        @JvmStatic fun parse(contcarFile: File): Contcar {
            var lineNumber = 1 // 当前解析到多少行
            var validLineNumber = 0 // 解析的行号
            val bufferedReader = BufferedReader(FileReader(contcarFile))
            var line: String? = bufferedReader.readLine()
            val contcar = Contcar()
            while (line != null) {
                if (line.isBlank()) {
                    // 排除空行
                    lineNumber++
                } else {
                    // 非空行
                    validLineNumber++ // 非空行情况下对lineNumber进行自增
                    try {
                        when(validLineNumber) {
                            1 -> parseName(line, contcar)
                            2 -> parseScale(line, contcar)
                            3 -> parseVectorA(line, contcar)
                            4 -> parseVectorB(line, contcar)
                            5 -> parseVectorC(line, contcar)
                            6 -> parseComponents(line, contcar)
                            7 -> parseComponentsNumber(line, contcar)
                            8 -> parseCoordinateType(line, contcar)
                            else -> parseComponentsCoordinate(line, contcar)
                        }

                    }catch(ex: Exception) {
                        throw ParseException("${ex.message} at line $lineNumber", lineNumber)
                    }
                }
                line = bufferedReader.readLine() // 读取下一行
            }
            // 返回最终结果
            return contcar
        }

    }
}