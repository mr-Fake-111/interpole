package Graphics_methods_package

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import src_current.Interpolator

fun graphByNodesSplineLinear(group: Group, properties: Array<DoubleArray>, widgth:Double, height:Double) {
    val polys = Interpolator.interpolationSplineLinear(properties)

    for (i in 1 until properties.size) { //выводим график для Линейного сплайна

        val line = Line(
            properties[i-1][0] * 5 + widgth / 2,
            -1 * 5 * polys[i-1].calculateAt(properties[i-1][0]) + height / 2,
            properties[i][0] * 5 + widgth / 2,
            -1 * 5 * polys[i-1].calculateAt(properties[i][0]) + height / 2
        )
        line.stroke = Color.RED
        group.children.add(
            line
        )

    }
}