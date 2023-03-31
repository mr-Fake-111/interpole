package src_current

import SLAE_methods_functions.*
import kotlin.math.pow

class Interpolator {
    companion object {

        fun interpolationPolynomialSLAE(nodes: Array<DoubleArray>): Polynomial {

            val n = nodes.size

            var matrix = Array<DoubleArray>(n) { DoubleArray(n) }
            var vector = Array<DoubleArray>(n) { DoubleArray(1) }
            for (i in 0..(n - 1)) {
                for (j in 0..(n - 1)) {
                    matrix[i][j] = nodes[i][0].pow(n - j - 1)
                }
            }
            for (i in 0..(n - 1)) {
                vector[i][0] = nodes[i][1]
            }

            val preCoefficients = SLAEAccurateMethods.ReflectionMethod(matrix, vector)
            val coefficients = MutableList<Double>(n) { 0.0 }
            for (i in 0..(n - 1)) {
                coefficients[i] = preCoefficients[i][0]
            }

            return Polynomial(coefficients)
        }

        //данный метод не возвращает полином в явном виде, а лишь сводит вычисление сложной функции к полиномиальным вычислениям
        //то есть возвращает значение в точке, обусловленное таблицей узлов и спецификой построения полинома
        //я мог бы перегрузить метод, чтобы он был хоть как-то применим за пределами задания, но .... пока не судьба
        fun interpolationPolynomialLagrange(nodes: Array<DoubleArray>, spot: Double): Double {
            var spotVal = 0.0

            for (i in 0..(nodes.size - 1)) {
                var currPart = 1.0
                for (j in 0..(i - 1)) {
                    currPart *= (spot - nodes[j][0]) / (nodes[i][0] - nodes[j][0])
                }
                for (j in (i + 1)..(nodes.size - 1)) {
                    currPart *= (spot - nodes[j][0]) / (nodes[i][0] - nodes[j][0])
                }
                currPart *= nodes[i][1]
                spotVal += currPart
            }

            return spotVal
        }

        //как и с предыдущим методом, в явном виде найти полином сложно, так что возвращаем только значение в точке
        fun interpolationPolynomialNewton(nodes: Array<DoubleArray>, spot: Double): Double {
            var spotVal = 0.0

            for (k in 0..(nodes.size - 1)) {
                var currSumPart = 0.0

                //использована альтернативная формула разделенной разности
                for (i in 0..k) {
                    var currMultPart = nodes[i][1]

                    for (j in 0..(i - 1)) {
                        currMultPart /= (nodes[i][0] - nodes[j][0])
                    }
                    for (j in (i + 1)..k) {
                        currMultPart /= (nodes[i][0] - nodes[j][0])
                    }
                    currSumPart += currMultPart
                }

                //после чего c найденной разделенной разностью производим домножение на множители вида (x-xi), где i = 0..(k-1)
                for (i in 0..(k - 1)) {
                    currSumPart *= (spot - nodes[i][0])
                }
                //добавляем слагаемое из предсталения полинома Ньютона в результирующую переменную
                spotVal += currSumPart
            }

            return spotVal
        }

        // построение полиномов линейного сплайна по заанным узлам
        fun interpolationSplineLinear(nodes: Array<DoubleArray>): Array<Polynomial> {

            val coefficients: DoubleArray = DoubleArray(2 * (nodes.size - 1)) { 0.0 }

            for (i in 0..2 * (nodes.size - 1) - 1 step 2) {

                coefficients[i] = (nodes[i / 2 + 1][1] - nodes[i / 2][1]) / (nodes[i / 2 + 1][0] - nodes[i / 2][0])
                coefficients[i + 1] =
                    (nodes[i / 2 + 1][1] * nodes[i / 2][0] - nodes[i / 2][1] * nodes[i / 2 + 1][0]) / (nodes[i / 2][0] - nodes[i / 2 + 1][0])
            }

            val polys = Array<Polynomial>(nodes.size - 1) { Polynomial() }
            for (i in 0..nodes.size - 2) {
                polys[i].coefficients = mutableListOf(coefficients[2 * i], coefficients[2 * i + 1])
            }

            return polys
        }

        fun interpolationSplineSquare(nodes: Array<DoubleArray>): Array<Polynomial> {

            //val coefficients: DoubleArray = DoubleArray(2 * (nodes.size - 1)) { 0.0 }

            var matrix = Array<DoubleArray>(3*(nodes.size-1)) { DoubleArray(3*(nodes.size-1)) {0.0} }
            var vector = Array<DoubleArray>(3*(nodes.size-1)) {DoubleArray(1) {0.0} }

            for(i in 0..3*(nodes.size-1) -1 step 3) {

                for(j in 0..2) {
                    matrix[i][i+j] = nodes[i / 3][0].pow(2-j)
                    matrix[i+1][i+j] = nodes[i/3 + 1][0].pow(2-j)
                }
                matrix[i+2][i]  = 2*nodes[i/3 + 1][0]
                matrix[i+2][i+1]  = 1.0
                if( i + 3 < 3*(nodes.size-1)) {
                    matrix[i+2][i+3]  = -2*nodes[i/3 + 1][0]
                    matrix[i+2][i+4]  = -1.0
                }

                vector[i][0] = nodes[i/3][1]
                vector[i+1][0] = nodes[i/3 + 1][1]

            }

            val preCoefficients = SLAEAccurateMethods.GaussMethod(matrix, vector)
            val coefficients = MutableList<Double>(preCoefficients.size) { 0.0 }
            for (i in 0..(preCoefficients.size - 1)) {
                coefficients[i] = preCoefficients[i][0]
            }

            val polys = Array<Polynomial>(nodes.size - 1) { Polynomial() }
            for (i in 0..nodes.size - 2) {
                polys[i].coefficients = mutableListOf(coefficients[3 * i], coefficients[3 * i + 1], coefficients[3*i + 2])
            }

            return polys

        }

        fun interpolationSplineCubic(nodes: Array<DoubleArray>): Array<Polynomial> {

            val n = nodes.size

            var matrixH = Array<DoubleArray>(nodes.size-2){ DoubleArray(nodes.size-2) {0.0} }
            var vectorGamma = Array<DoubleArray>(nodes.size-2){DoubleArray(1) {0.0} }

            for(i in 0..nodes.size-4) {
                matrixH[i][i] = 2*(nodes[i+2][0] - nodes[i][0])
                matrixH[i+1][i] = nodes[i+2][0] - nodes[i+1][0]
                matrixH[i][i+1] = nodes[i+2][0] - nodes[i+1][0]

                vectorGamma[i][0] = 6* ((nodes[i+2][1] - nodes[i+1][1])/(nodes[i+2][0] - nodes[i+1][0]) - (nodes[i+1][1] - nodes[i][1])/(nodes[i+1][0] - nodes[i][0]))
            }
            vectorGamma[nodes.size-3][0] = 6* ((nodes[n-1][1] - nodes[n-2][1])/(nodes[n-1][0] - nodes[n-2][0]) - (nodes[n-2][1] - nodes[n-3][1])/(nodes[n-2][0] - nodes[n-3][0]))
            matrixH[nodes.size-3][nodes.size-3] = 2*(nodes[nodes.size-1][0] - nodes[nodes.size-3][0])

            val secDerivatives = SLAEAccurateMethods.ReflectionMethod(matrixH, vectorGamma).toMutableList()
            secDerivatives.add(0, DoubleArray(1){0.0})
            secDerivatives.add( DoubleArray(1){0.0})

            val firDerivatives = MutableList<DoubleArray>(n-1) {DoubleArray(1){0.0} }
            for(i in 0..n-2) {
                firDerivatives[i][0] = (nodes[i+1][1] - nodes[i][1])/(nodes[i+1][0] - nodes[i][0]) - secDerivatives[i+1][0]*(nodes[i+1][0]- nodes[i][0])/6 - secDerivatives[i][0]*(nodes[i+1][0] - nodes[i][0])/3
            }


            val polys = Array<Polynomial>(n-1) {Polynomial()}
            for(i in 0..n-2) {
                val coefficients = MutableList<Double>(4) {0.0}
                coefficients[0] = (secDerivatives[i+1][0] - secDerivatives[i][0])/(6*(nodes[i+1][0] - nodes[i][0]))
                coefficients[1] = secDerivatives[i][0]/2 - (secDerivatives[i+1][0] - secDerivatives[i][0])/(2*(nodes[i+1][0] - nodes[i][0]))*nodes[i][0]
                coefficients[2] = firDerivatives[i][0] - secDerivatives[i][0]*nodes[i][0] + (secDerivatives[i+1][0] - secDerivatives[i][0])/(2*(nodes[i+1][0] - nodes[i][0]))*nodes[i][0].pow(2.0)
                coefficients[3] = nodes[i][1] - firDerivatives[i][0]*nodes[i][0] + secDerivatives[i][0]/2*nodes[i][0].pow(2.0) - (secDerivatives[i+1][0] - secDerivatives[i][0])/(6*(nodes[i+1][0] - nodes[i][0]))*nodes[i][0].pow(3.0)
                polys[i].coefficients = coefficients
            }
            return polys

        }
    }
}