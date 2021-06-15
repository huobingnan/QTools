package openq.model


/**
 * Gaussian计算输出log文件对象
 * input：计算输入文件
 * output：计算输出文件
 * routine：计算例程
 * calculationLevel: 计算水平
 * calculationBasicGroup：计算使用的基组
 * charge：计算体系所带的电荷数目
 * initAtomCoordinateList：初始原子坐标列表
 * lastAtomCoordinateList: 优化之后的原子坐标列表
 * maximumForceReached: 最大力是否收敛到阈值
 * RMSForceReached：RMS力是否收敛到阈值
 * maximumDisplacementReached：最大位移是否收敛到阈值
 * RMSDisplacementReached： RMS是否收敛到阈值
 */
data class GaussianLog(var input: String = "undefined",
                       var output: String = "undefined",
                       var routine: String = "undefined",
                       var calculationLevel: String = "undefined",
                       var calculationBasicGroup: String = "undefined",
                       var charge: Int = 0,
                       var initAtomCoordinateList: ArrayList<AtomCoordinate> = ArrayList(),
                       var lastAtomCoordinateList: ArrayList<AtomCoordinate> = ArrayList(),
                       var maximumForceReached: Boolean = false,
                       var RMSForceReached: Boolean = false,
                       var maximumDisplacementReached: Boolean = false,
                       var RMSDisplacementReached: Boolean = false,
) {
    override fun toString(): String {
        return "GaussianLog(input='$input', output='$output', routine='$routine', calculationLevel='$calculationLevel', calculationBasicGroup='$calculationBasicGroup', charge=$charge, maximumForceReached=$maximumForceReached, RMSForceReached=$RMSForceReached, maximumDisplacementReached=$maximumDisplacementReached, RMSDisplacementReached=$RMSDisplacementReached)"
    }
}