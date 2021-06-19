package openq

import org.jblas.DoubleMatrix
import org.junit.Test

open class JBlasTest {

    @Test
    fun test1() {
        val base = DoubleMatrix(3,3 ,
            1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0
        )

        val v = DoubleMatrix(3, 1, 1.0, 1.0, 1.0)

        print(base.mmul(v).toString())
    }
}