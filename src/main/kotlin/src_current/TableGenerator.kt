package src_current

import java.lang.Math.abs
import java.lang.Math.max

class TableGenerator {
    companion object {
        fun getTableLineByNodes(nodes: Array<DoubleArray>, polyType: String): MutableList<DoubleArray> {

            val  n = nodes.size

            if(!(polyType in arrayOf("Newton", "Lagrange"))) {
                print("Incorrect poly type")
                return MutableList<DoubleArray>(0) { DoubleArray(0) }
            }

            val midSpots =MutableList<DoubleArray>(0) {DoubleArray(2) {0.0} }
            for(i in 1 until n) {

                var j = nodes[i-1][0]
                while(j + 0.01 < nodes[i][0]) {

                    midSpots.add(DoubleArray(2) {0.0})
                    midSpots[midSpots.size-1][0] = j

                    when (polyType) {
                        "Newton" -> midSpots[midSpots.size-1][1] = Interpolator.interpolationPolynomialNewton(nodes, midSpots[midSpots.size-1][0])
                        "Lagrange" -> midSpots[midSpots.size-1][1] = Interpolator.interpolationPolynomialLagrange(nodes, midSpots[midSpots.size-1][0])
                    }
                    j+=0.01
                }
            }

            var maxMismatch = 0.0
            for(i in 0..midSpots.size-2) {
                val mismatch: Double = abs(midSpots[i][1] - UserFunction.calculate(midSpots[i][0]))
                if(maxMismatch < mismatch) maxMismatch = mismatch
            }

            println("$n ${midSpots.size} ${"%.8f".format(maxMismatch)}")

            return midSpots;
        }

        fun getTableLineByNodesSpline(nodes: Array<DoubleArray>, polys: Array<Polynomial>): MutableList<DoubleArray> {

            val midSpots = MutableList<DoubleArray>(0) {DoubleArray(2) {0.0} }

            for (i in 1 until nodes.size) {

                var j = nodes[i-1][0]
                while(j + 0.01 < nodes[i][0]) {

                    midSpots.add(DoubleArray(2) {0.0})
                    midSpots[midSpots.size-1][0] = j
                    midSpots[midSpots.size-1][1] = polys[i-1].calculateAt(j)

                    j+=0.01
                }


            }

            var maxMismatch = 0.0
            for(i in 0..midSpots.size-2) {
                val mismatch: Double = abs(midSpots[i][1] - UserFunction.calculate(midSpots[i][0]))
                if(maxMismatch < mismatch) maxMismatch = mismatch
            }

            println("${nodes.size} ${midSpots.size} ${"%.8f".format(maxMismatch)}")

            return midSpots
        }
    }

}