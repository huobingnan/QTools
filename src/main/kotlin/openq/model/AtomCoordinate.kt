package openq.model

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
