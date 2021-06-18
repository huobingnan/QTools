package openq.model

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
                coordinates.add(
                    AtomCoordinate(
                        componentNow,
                        componentCnt,
                        0,
                        contcar.componentsCoordinate[i][0],
                        contcar.componentsCoordinate[i][1],
                        contcar.componentsCoordinate[i][2]
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