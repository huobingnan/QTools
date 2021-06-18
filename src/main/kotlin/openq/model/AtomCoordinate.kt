package openq.model

/**
 * 原子坐标类，这个类将是一个通用的类模板。
 * 不管是任何的计算软件的输出，无论其采用何种坐标表示形式，
 * 都要实现一定的算法，将其输出的各个原子的坐标，转换为此
 * 形式的坐标类型。
 * 这里的x，y，z数值为以
 *  1 0 0
 *  0 1 0
 *  0 0 1
 * 为坐标系基矢下的坐标数值。
 * 具体的转换算法，将由Resource类去实现
 */
data class AtomCoordinate(var symbol: String = "not record",
                          var sequenceNumber: Int = 0,
                          var elementNumber: Int = 0,
                          var x: Double = 0.0,
                          var y: Double = 0.0,
                          var z: Double = 0.0) {
    override fun toString(): String {
        return "AtomCoordinate(symbol='$symbol', sequenceNumber=$sequenceNumber, elementNumber=$elementNumber, x=$x, y=$y, z=$z)"
    }
}
