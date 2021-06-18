package openq.model

import openq.constants.CoordinateType
import org.jblas.DoubleMatrix
import org.slf4j.LoggerFactory

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
        private val log = LoggerFactory.getLogger(Resource::class.java)
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
            // 构造基矢
            val base = DoubleMatrix(3,3,
                contcar.matrix!![0][0], contcar.matrix!![1][0], contcar.matrix!![2][0],
                contcar.matrix!![0][1], contcar.matrix!![1][1], contcar.matrix!![2][1],
                contcar.matrix!![0][2], contcar.matrix!![1][2], contcar.matrix!![2][2],
            )
            if (log.isDebugEnabled) {
                log.debug("晶胞基矢初始化完毕！")
                log.debug("基矢 : {}", base.toString())
            }
            while (i < size) {
                val componentNow = contcar.componentsNameList!![componentCursor]
                // BUGFIX 2021-6-18 如果是Fraction类型的坐标，需要进行一定的转换才可以使用
                var coordinate = contcar.componentsCoordinate[i]
                if (contcar.coordinateType == CoordinateType.Direct) {
                    // 分数坐标要进行转换
                    coordinate = base.mulColumnVector(
                        DoubleMatrix(3,1, coordinate[0], coordinate[1], coordinate[2])
                    ).toArray().toTypedArray()
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