package openq.vasp

import openq.model.*
import org.jblas.DoubleMatrix
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.log

/**
 * 键长分析模块
 */
class BondLength {


    companion object {

        private val log = LoggerFactory.getLogger(BondLength::class.java)

        @JvmStatic fun componentCombinationSelect(num: Int, componentList: List<String>): ArrayList<ArrayList<String>> {
            val result: ArrayList<ArrayList<String>>  = ArrayList()
            if (num == 1) {
                for (c in componentList) {
                    val list: ArrayList<String> = ArrayList()
                    list.add(c)
                    result.add(list)
                }
                return result
            }
            if (num >= componentList.size) {
                return result
            }
            val size = componentList.size
            for (i in 0 until size - num + 1) {
                //从i+1处直至字符串末尾
                val cr: List<ArrayList<String>> = componentCombinationSelect(num - 1, componentList.subList(i + 1, size))
                val c = componentList[i] //得到上面被去掉的字符，进行组合
                for (s in cr) {
                    s.add(c)
                    result.add(s)
                }
            }
            return result
        }


        /**
         * 执行键长变化分析
         */
        fun perform(analyseKeyFrameList: List<AnalyseKeyFrame>, resourceCache: Map<String, Resource>,
                    channelSetting: ChannelSetting):BondLengthResult {

            /*
                最终计算的结果，帧的查找键为帧在数组中的索引
            */
            val bondDistanceResult = java.util.ArrayList<MutableMap<String, Double>>()


            // 先设置为默认值
            var distanceMax: Double = java.lang.Double.parseDouble(
                ExtChannelSettings.getDefaultValue(ExtChannelSettings.BOND_LENGTH_MAX)
            )

            if (channelSetting.extSetting.containsKey(ExtChannelSettings.BOND_LENGTH_MAX)) {
                try {
                    distanceMax = channelSetting.extSetting[ExtChannelSettings.BOND_LENGTH_MAX]!!.toDouble()
                } catch (ignored: Exception) {
                    if (log.isDebugEnabled) {
                        log.error("${ignored.message}")
                    }
                }
            }

            for (i in analyseKeyFrameList.indices) {
                // 初始化结果集
                bondDistanceResult.add(java.util.HashMap())
                // 计算
                val frame = analyseKeyFrameList[i]
                val resource = resourceCache[frame.resourceName]!!
                // 转换为通用坐标形式
                val commonCoordinate = Resource.convertCoordinateToCommonType(resource)
                val componentList: Set<String> = commonCoordinate.map {
                    "${it.symbol}${it.sequenceNumber}"
                }.toSet()
                if (log.isDebugEnabled) {
                    log.debug("组分集合：{}", componentList.toString())
                }
                // 查找表
                val componentAndCoordinate = HashMap<String, DoubleArray>()
                commonCoordinate.forEach {
                    componentAndCoordinate["${it.symbol}${it.sequenceNumber}"] = doubleArrayOf(it.x, it.y, it.z)
                }
//                if (log.isDebugEnabled) {
//                    log.debug(frame.name)
//                    componentAndCoordinate.forEach { (k, v) ->
//                        log.debug("{} => {}", k, v.contentToString())
//                    }
//                }

                // 对计算体系中的原子进行组合，得到候选化学键集合
                val combinationResult: List<List<String>> =
                    componentCombinationSelect(2, java.util.ArrayList(componentList))
                // 计算出候选化学键的键长
                for (combination in combinationResult) {
                    val component1 = combination[0]
                    val component2 = combination[1]
                    val component1Coordinate = componentAndCoordinate[component1]!!
                    val component2Coordinate = componentAndCoordinate[component2]!!
                    var distance = 0.0 // 键长

                    // 坐标转换并计算
                    val coor1 = DoubleMatrix(
                        3, 1, component1Coordinate[0],
                        component1Coordinate[1], component1Coordinate[2]
                    )

                    val coor2 = DoubleMatrix(
                        3, 1, component2Coordinate[0],
                        component2Coordinate[1], component2Coordinate[2]
                    )

                    distance = coor1.sub(coor2).norm2()

                    // 检查该键长是否已经超出了化学成键距离的阈值
                    if (distance <= distanceMax) {
                        // 放入结果
                        bondDistanceResult[i]["$component1-$component2"] = distance
                    }
                }
            }
            return BondLengthResult(analyseKeyFrameList, bondDistanceResult)
        }
    }
}