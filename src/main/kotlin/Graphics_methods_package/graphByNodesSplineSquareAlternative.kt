package Graphics_methods_package

import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import src_current.Interpolator

fun graphByNodesSplineSquareAlternative(group: Group, properties: Array<DoubleArray>, widgth:Double, height:Double) {
    val polys = Interpolator.interpolationSplineSquareAlternative(properties)

    for (i in 1 until properties.size) { //выводим график для Квадратичного сплайна

        var j = properties[i-1][0]
        while(j + 1 < properties[i][0]) {
            val line = Line(
                j * 5 + widgth / 2,
                -1 * 5 * polys[i - 1].calculateAt(j) + height / 2,
                (j+1) * 5 + widgth / 2,
                -1 * 5 * polys[i - 1].calculateAt(j+1) + height / 2
            )
            line.stroke = Color.RED
            group.children.add(
                line
            )

            j+=1
        }
        val line = Line(
            j * 5 + widgth / 2,
            -1 * 5 * polys[i - 1].calculateAt(j) + height / 2,
            properties[i][0] * 5 + widgth / 2,
            -1 * 5 * polys[i - 1].calculateAt(properties[i][0]) + height / 2
        )
        line.stroke = Color.RED
        group.children.add(
            line
        )

    }
}