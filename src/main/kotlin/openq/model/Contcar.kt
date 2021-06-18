package openq.model

import openq.constants.CoordinateType

/**
 * VASP坐标文件形式
 * name：VASP计算例程的名字
 * scale：缩放比例
 * matrix：晶胞矩阵
 * componentsNameList：元素名称列表
 * componentNumberList：元素数量列表
 * coordinateType：坐标类型：分为分数坐标和笛卡尔坐标两种形式
 * componentAmount: 元素数量
 * componentCoordinates: 元素的坐标
 */
data class Contcar(
    var name: String,
    var scale: Double,
    var matrix: Array<Array<Double>>?,
    var componentsNameList: List<String>?,
    var componentsNumberList: List<Int>?,
    var coordinateType: CoordinateType,
    var componentAmount: Int = 0,
    var componentsCoordinate: ArrayList<Array<Double>> = ArrayList()
    ) {

    constructor() : this("unknown", 1.0, null, null, null, CoordinateType.Cartesian,)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contcar

        if (name != other.name) return false
        if (scale != other.scale) return false
        if (!matrix.contentDeepEquals(other.matrix)) return false
        if (componentsNameList != other.componentsNameList) return false
        if (componentsNumberList != other.componentsNumberList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + matrix.contentDeepHashCode()
        result = 31 * result + componentsNameList.hashCode()
        result = 31 * result + componentsNumberList.hashCode()
        return result
    }
}