package openq.vasp

import openq.model.AtomCoordinate
import openq.model.GaussianLog
import openq.util.ElementTable
import openq.util.Strings
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.lang.StringBuilder
import java.text.ParseException

/**
 * Gaussian文件解析
 */
class GaussianFileParser {
    companion object {
        // 阈值条目检索正则表达式
        private val thresholdTokenRegexp = Regex("Item[ ]+Value[ ]+Threshold[ ]+Converged[?]")

        private val atomCoordinateTokenRegex = Regex("Center[ ]+Atomic[ ]+Atomic[ ]+Coordinates[ ]+\\(Angstroms\\)")

        // 解析计算输入文件
        private val parseCalculationInputFile = {line: String, gaussianLog: GaussianLog ->
            val splitContents = line.split("=")
            if (splitContents.size == 2) {
                gaussianLog.input = splitContents[1]
            }
        }

        // 解析计算输出文件
        private val parseCalculationOutputFile = {line: String, gaussianLog: GaussianLog ->
            val splitContents = line.split("=")
            if (splitContents.size == 2) {
                gaussianLog.output = splitContents[1]
            }
        }

        // 解析计算例程
        private val parseCalculationRoutine = {line: String, gaussianLog: GaussianLog ->
            val splitContent = line.split(" ")
            if (splitContent.isNotEmpty()) {
                // 正常来说，这里会有内容
                // 掐头去尾，代表计算的内容，数组最末尾代表计算所使用的计算水平与计算基组
                val calculationContentBuilder = StringBuilder()
                splitContent.subList(1, splitContent.size - 1).forEach{ s ->
                    calculationContentBuilder.append(s)
                    calculationContentBuilder.append(' ')
                }
                gaussianLog.routine = calculationContentBuilder.toString().trim()

                // 解析计算水平和计算基组
                val levelAndBasicGroup = splitContent.last().split("/")
                if (levelAndBasicGroup.size == 2) {
                    gaussianLog.calculationLevel = levelAndBasicGroup[0]
                    gaussianLog.calculationBasicGroup = levelAndBasicGroup[1]
                }
            }
        }

        // 解析阈值信息
        private val parseThreshold = {reader: BufferedReader, gaussianLog: GaussianLog ->
            val maximumForceCandidate = Strings.splitStringByAnyBlanks(reader.readLine().trim())
            val RMSForceCandidate = Strings.splitStringByAnyBlanks(reader.readLine().trim())
            val maximumDisplacementCandidate = Strings.splitStringByAnyBlanks(reader.readLine().trim())
            val RMSDisplacementCandidate = Strings.splitStringByAnyBlanks(reader.readLine().trim())

            gaussianLog.maximumDisplacementReached = (maximumDisplacementCandidate.isNotEmpty()
                    && maximumDisplacementCandidate.last().toUpperCase() == "YES")
            gaussianLog.maximumForceReached = (maximumForceCandidate.isNotEmpty()
                    && maximumForceCandidate.last().toUpperCase() == "YES")
            gaussianLog.RMSForceReached = (RMSForceCandidate.isNotEmpty()
                    && RMSForceCandidate.last().toUpperCase() == "YES")
            gaussianLog.RMSDisplacementReached = (RMSDisplacementCandidate.isNotEmpty()
                    && RMSDisplacementCandidate.last().toUpperCase() == "YES")
            // 返回下一行
            reader.readLine()
        }

        // 解析坐标信息
        private val parseCoordinate = {reader: BufferedReader, gaussianLog: GaussianLog ->
            reader.readLine(); reader.readLine() //跳过表头
            var line = reader.readLine()
            val list = ArrayList<AtomCoordinate>()
            while (true) {
                val candidate = Strings.splitStringByAnyBlanks(line.trim())
                if (candidate.size == 6) {
                    // 这是正常情况,分隔结束之后会有六部分的数据，分别为
                    // 原子编号，元素序号，是否固定，x，y，z
                    val sequenceNumber = Integer.parseInt(candidate[0])
                    val elementNumber = Integer.parseInt(candidate[1])
                    val x = java.lang.Double.parseDouble(candidate[3])
                    val y = java.lang.Double.parseDouble(candidate[4])
                    val z = java.lang.Double.parseDouble(candidate[5])
                    if (gaussianLog.lastAtomCoordinateList.isEmpty()) {
                        list.add(
                            AtomCoordinate(
                                ElementTable.getByElementNumber(elementNumber),
                                sequenceNumber,
                                elementNumber,
                                x,y,z
                            )
                        )
                    }else {
                        val index = sequenceNumber - 1
                        gaussianLog.lastAtomCoordinateList[index].x = x
                        gaussianLog.lastAtomCoordinateList[index].y = y
                        gaussianLog.lastAtomCoordinateList[index].z = z
                    }
                    line = reader.readLine()
                }else {
                    if (gaussianLog.lastAtomCoordinateList.isEmpty())
                        gaussianLog.lastAtomCoordinateList = list
                    break
                }
            }
            line // 将读取行返回

        }

        @JvmStatic fun parse(gaussianLogFile: File): GaussianLog{
            val gaussianLog = GaussianLog()
            // 解析控制条件
            var lineNumber = 0 // 解析文件的行号
            var validLineNumber = 0 // 有效行号（排除空格）
            var currentLine: String? = null // 解析的当前行

            // 使用bufferedReader按行读取gaussian文件
            val bufferedReader = BufferedReader(FileReader(gaussianLogFile))
            currentLine = bufferedReader.readLine()

            while (currentLine != null) {
                // 处理currentLine
                currentLine = currentLine.trim()
                // 校验currentLine
                if (currentLine.isNotBlank()){
                    validLineNumber++
                } else {
                    currentLine = bufferedReader.readLine()
                    continue
                }
                lineNumber++
                // 解析业务实现部分
                try {
                    if (currentLine.startsWith("Input")) {
                        // 处理输入
                        parseCalculationInputFile(currentLine, gaussianLog)
                        currentLine = bufferedReader.readLine()
                        continue
                    }
                    if (currentLine.startsWith("Output")) {
                        // 处理计算输出文件
                        parseCalculationOutputFile(currentLine, gaussianLog)
                        currentLine = bufferedReader.readLine()
                        continue
                    }
                    if (currentLine.startsWith("#p")) {
                        // 处理计算例程信息
                        parseCalculationRoutine(currentLine, gaussianLog)
                        currentLine = bufferedReader.readLine()
                        continue
                    }
                    if (currentLine.matches(thresholdTokenRegexp)) {
                        // 处理阈值收敛信息
                        currentLine = parseThreshold(bufferedReader, gaussianLog)
                        continue
                    }
                    if (currentLine.matches(atomCoordinateTokenRegex)) {
                        // 处理坐标信息
                        currentLine = parseCoordinate(bufferedReader, gaussianLog)
                        continue
                    }
                    // 不满足以上解析条件，直接读取下一行
                    currentLine = bufferedReader.readLine()
                }catch (ex: Exception) {
                    throw ParseException("An error occur while parsing at line number $lineNumber", lineNumber)
                }
            }
            return gaussianLog
        }
    }
}