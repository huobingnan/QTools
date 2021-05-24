package openq.vasp

import openq.model.AnalyseKeyFrame
import openq.model.ChannelSetting
import openq.model.Contcar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BondLength {
    companion object {

        @JvmStatic
        private fun componentCombinationSelect(num: Int, componentList: List<String>): ArrayList<ArrayList<String>> {
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

        fun perform(analyseKeyFrameList: List<AnalyseKeyFrame>, resourceCache: Map<String, Contcar>,
                    channelSetting: ChannelSetting):List<Map<String, Double>> {

            val result = ArrayList<HashMap<String, Double>>()


            return result
        }
    }
}