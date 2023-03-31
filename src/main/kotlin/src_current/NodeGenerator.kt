package src_current

import java.lang.Math.cos


class NodeGenerator {
    companion object {
        fun getOptimalNodes(left: Double, right: Double, count: Int): MutableList<Double> {
            val xValues = MutableList<Double>(count) { 0.0 }
            for (i in count downTo 1) {
                xValues[count - i] =
                    0.5 * ((right - left) * cos((2 * i.toDouble() - 1) / (2 * count) * Math.PI) + (right + left))
            }

            return xValues
        }

        fun getStandartNodes(left: Double, right: Double, count: Int): MutableList<Double> {
            val xValues = MutableList<Double>(count) { 0.0 }
            for (i in 0..(count - 1)) {
                xValues[i] = left + (right - left) * i / (count - 1)
            }

            return xValues
        }
    }
}