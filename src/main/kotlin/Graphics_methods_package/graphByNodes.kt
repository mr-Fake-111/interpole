package Graphics_methods_package

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import src_current.Interpolator
import src_current.Interpolator.Companion.interpolationSplineLinear
import src_current.NodeGenerator
import src_current.UserFunction
import java.lang.Math.abs

fun graphByNodes(group: Group, properties: Array<DoubleArray>, widgth:Double, height:Double, polyType: String) {

    if(polyType == "Newton") {
        for (i in (-widgth / 2).toInt() / 5..((widgth / 2).toInt() - 1) / 5) { //выводим график для Ньютона
            val line = Line(
                i * 5 + widgth / 2,
                -1 * 5 * Interpolator.interpolationPolynomialNewton(properties, i.toDouble()) + height / 2,
                (i + 1) * 5 + widgth / 2,
                -1 * 5 * Interpolator.interpolationPolynomialNewton(properties, (1 + i).toDouble()) + height / 2
            )
            line.stroke = Color.RED
            group.children.add(
                line
            )
        }
    } else if(polyType == "Lagrange") {
        for (i in (-widgth / 2).toInt() / 5..((widgth / 2).toInt() - 1) / 5) { //выводим график для Ньютона по стандартным узлам
            val line = Line(
                i * 5 + widgth / 2,
                -1 * 5 * Interpolator.interpolationPolynomialLagrange(properties, i.toDouble()) + height / 2,
                (i + 1) * 5 + widgth / 2,
                -1 * 5 * Interpolator.interpolationPolynomialLagrange(properties, (1 + i).toDouble()) + height / 2
            )
            line.stroke = Color.RED
            group.children.add(
                line
            )
        }
    } else if(polyType == "Newton_alternative") {
        for (i in (-widgth / 2).toInt() / 5..((widgth / 2).toInt() - 1) / 5) { //выводим график для Ньютона
            val line = Line(
                i * 5 + widgth / 2,
                -1 * 5 * Interpolator.interpolationPolynomialNewtonAlternative(properties, i.toDouble()) + height / 2,
                (i + 1) * 5 + widgth / 2,
                -1 * 5 * Interpolator.interpolationPolynomialNewtonAlternative(properties, (1 + i).toDouble()) + height / 2
            )
            line.stroke = Color.RED
            group.children.add(
                line
            )
        }
    }
    else if(polyType == "Mismatch"){
        for (i in 0..properties.size - 2) {
            val line = Line(
                 5*properties[i][0] + widgth / 2,
                -5*kotlin.math.abs(properties[i][1] - UserFunction.calculate(properties[i][0])) + height / 2,
                 5*(properties[i+1][0]) + widgth / 2,
                -5*kotlin.math.abs(properties[i + 1][1] - UserFunction.calculate(properties[i+1][0])) + height / 2
            )
            line.stroke = Color.BLUE
            group.children.add(
                line
            )
        }
    } else {
        print("Указанный тип полинома не имеет текущей реализации или не существует")
    }
}
