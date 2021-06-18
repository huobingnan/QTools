package openq.model

import openq.constants.CoordinateType
import org.jblas.DoubleMatrix

/**
 * 解析的文件资源
 * name：文件的唯一表示名称
 * type：文件的类型
 * instance：具体文件的展现类
 */
class Resource {
    var name: String = "undefined"
    var type: String = "unsorted"
    var instance: Any? = null

    companion object {
        const val VASP = "vasp"
        const val GAUSSIAN = "gaussian"


        /**
         * 将contcar坐标，转换为通用的AtomCoordinate形式
         */
        private fun convertContcarCoordinateToCommonType(contcar: Contcar): List<AtomCoordinate> {
            val coordinates = ArrayList<AtomCoordinate>()
            var i = 0
            val size = contcar.componentsCoordinate.size
            var componentCnt = 1
            var componentCursor = 0
            while (i < size) {
                val componentNow = contcar.componentsNameList!![componentCursor]
                // BUGFIX 2021-6-18 如果是Fraction类型的坐标，需要进行一定的转换才可以使用
                var coordinate = contcar.componentsCoordinate[i]
                val base = DoubleMatrix(3,3)
                // TODO 构造基矢
                if (contcar.coordinateType == CoordinateType.Direct) {

                }
                coordinates.add(
                    AtomCoordinate(
                        componentNow,
                        componentCnt,
                        0,
                        coordinate[0],
                        coordinate[1],
                        coordinate[2]
                    )
                )
                i++
                componentCnt++
                if (componentCnt > contcar.componentsNumberList!![componentCursor]) {
                    componentCnt = 1
                    componentCursor++
                }
            }
            return coordinates
        }

        /**
         * 将不同形式的resource中的坐标转换为一个通用的形式
         */
        fun convertCoordinateToCommonType(resource: Resource): List<AtomCoordinate> {
            if (resource.type == VASP) {
                // 将VASP坐标形式转换为通用的坐标表示形式
                val instance = resource.instance as Contcar
                return convertContcarCoordinateToCommonType(instance)
            } else if (resource.type == GAUSSIAN) {
                return (resource.instance as GaussianLog).lastAtomCoordinateList
            } else {
                return emptyList()
            }
        }
    }
}