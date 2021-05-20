package openq.vasp
import openq.constants.CoordinateType
import openq.model.Contcar
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.text.ParseException
import kotlin.collections.ArrayList

class ContcarFileParser {

    companion object {
        private val parseName = {line: String, contcar: Contcar -> contcar.name = line.trim()}

        private val parseScale = {line: String, contcar: Contcar -> contcar.scale = line.toDouble()}

        private val parseVectorA = {line: String, contcar: Contcar ->
            val vectorA = splitStringByAnyBlanks(line)
            // 初始化矩阵
            contcar.matrix = Array(3) {Array(3){0.0} }
            for (i in vectorA.indices) {
                contcar.matrix!![0][i] = vectorA[i].toDouble()
            }
        }

        // 解析晶胞向量 b
        private val parseVectorB = {line: String, contcar: Contcar ->
            val vectorB = splitStringByAnyBlanks(line)
            for (i in vectorB.indices) {
                contcar.matrix!![1][i] = vectorB[i].toDouble()
            }

        }

        // 解析晶胞向量 c
        private val parseVectorC = {line: String, contcar: Contcar ->
            val vectorC = splitStringByAnyBlanks(line)
            for (i in vectorC.indices) {
                contcar.matrix!![2][i] = vectorC[i].toDouble()
            }
        }

        // 解析化学体系中的组分
        private val parseComponents = {line: String, contcar: Contcar ->
            contcar.componentsNameList = splitStringByAnyBlanks(line)
        }

        // 解析化学体系中各个组分的数目
        private val parseComponentsNumber = {line: String, contcar: Contcar ->
            contcar.componentsNumberList = splitStringByAnyBlanks(line).map { it.toInt() }
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
                val coordinate = splitStringByAnyBlanks(line)
                val coordinateDoubleArray = coordinate.map { it.toDouble() }.toTypedArray()
                contcar.componentsCoordinate.add(coordinateDoubleArray)
                contcar.componentAmount++
            }
        }


        /**
         * 将一个字符串按照任意数量的空格进行分隔
         * @param str 输入字符串
         * @return 分隔完毕之后的字符串数组
         */
        private fun splitStringByAnyBlanks(str: String?): List<String> {
            val res = ArrayList<String>()
            if (str != null) {
                val charArray = str.toCharArray()
                val stringBuilder = StringBuilder()

                for (ch in charArray) {
                    if (ch != ' ') {
                        stringBuilder.append(ch)
                    } else {
                        // 添加到res中
                        if (stringBuilder.isNotEmpty()) {
                            res.add(stringBuilder.toString())
                            stringBuilder.setLength(0)
                        }
                    }
                }
                if (stringBuilder.isNotEmpty()) {
                    res.add(stringBuilder.toString())
                }
            }
            return res
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