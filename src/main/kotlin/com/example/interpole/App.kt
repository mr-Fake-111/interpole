package com.example.interpole

import Graphics_methods_package.*
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.stage.Stage
import src_current.Interpolator
import src_current.NodeGenerator
import src_current.TableGenerator
import src_current.UserFunction

class App: Application() {

    private val HEIGHT = 720.0
    private val WIDGTH = 1080.0

    override fun start(primaryStage: Stage?) {
        val group = Group()
        val scene = Scene(group, 1080.0, 720.0)

        group.children.addAll(
            Line(0.0, HEIGHT/2, WIDGTH, HEIGHT/2),
            Line(WIDGTH/2, 0.0, WIDGTH/2, HEIGHT)
        )

        val oper: (Double) -> Double = { x: Double -> Math.pow(x, 2.0) + 4*Math.sin(x) -2 } //лямбда для отстроение функции

        for(i in (-WIDGTH/2).toInt()/5.. ((WIDGTH/2).toInt()-1)/5) {
            group.children.add(
                Line(i*5 + WIDGTH/2, -1*5*oper(i.toDouble()) + HEIGHT/2, (i+1)*5 + WIDGTH/2, -1*5*oper((i+1).toDouble()) + HEIGHT/2)
            )
        }

        val standNodes = NodeGenerator.getStandartNodes(-10.0, 10.0, 20) //задание узлов для полинома лагранжа/Ньютона
        //при вычислении сплайнов лажают методы СЛАУ (видимо, конвертация некорректная вышла, но дебажить я уже не успею). При >10 узлах почти всегда нормально работает
        val standProperties = UserFunction.getNodes(standNodes)

        val optNodes = NodeGenerator.getOptimalNodes(-10.0, 10.0, 10) //задание узлов для полинома лагранжа/Ньютона
        val optProperties = UserFunction.getNodes(optNodes)

        val polys = Interpolator.interpolationSplineCubic(optProperties)

        //graphByNodes(group, standProperties, WIDGTH, HEIGHT, "Newton") //построение графика интерполяционного полинома Ньютона/Лагранжа
        //graphByNodes(group, TableGenerator.getTableLineByNodes(standProperties, "Newton").toTypedArray(), WIDGTH, HEIGHT, "Mismatch")

        //graphByNodesSplineLinear(group, standProperties, WIDGTH, HEIGHT) //Построение линейного сплайна

        //graphByNodesSplineSquare(group, optProperties, WIDGTH, HEIGHT) //посторение квадратичного сплайна

        graphByNodesSplineCubic(group, standProperties, WIDGTH, HEIGHT) //построение кубического сплайна

        graphByNodes(group, TableGenerator.getTableLineByNodesSpline(optProperties, polys).toTypedArray(), WIDGTH, HEIGHT, "Mismatch")

        primaryStage?.scene = scene
        primaryStage?.show()
    }


    fun launchApp() {
        launch()
    }

}


fun main(args: Array<String>) {
    val app = App()
    app.launchApp()

    /*println("Header: Newton Standart\n")
        for(i in 10..80) {
           val nodes = NodeGenerator.getStandartNodes(-5.0, 5.0, i)
           val properties = UserFunction.getNodes(nodes)
           TableGenerator.getTableLineByNodes(properties, "Newton")

        }
        println()

        println("Header: Newton Optimal\n")
        for(i in 10..80) {
            val nodes = NodeGenerator.getOptimalNodes(-5.0, 5.0, i)
            val properties = UserFunction.getNodes(nodes)
            TableGenerator.getTableLineByNodes(properties, "Newton")

        }
        println()

        println("Header: Lagrange Standart\n")
        for(i in 10..80) {
            val nodes = NodeGenerator.getStandartNodes(-5.0, 5.0, i)
            val properties = UserFunction.getNodes(nodes)
            TableGenerator.getTableLineByNodes(properties, "Lagrange")

        }
        println()

        println("Header: Lagrange Optimal\n")
        for(i in 10..80) {
            val nodes = NodeGenerator.getOptimalNodes(-5.0, 5.0, i)
            val properties = UserFunction.getNodes(nodes)
            TableGenerator.getTableLineByNodes(properties, "Lagrange")

        }
        println()*/

    /////////////////////////////////////////////////

    println("Header: Spline Linear Standart\n")
    for(i in 10..80) {
        val nodes = NodeGenerator.getStandartNodes(-5.0, 5.0, i)
        val properties = UserFunction.getNodes(nodes)
        val polys = Interpolator.interpolationSplineLinear(properties)
        TableGenerator.getTableLineByNodesSpline(properties, polys)

    }
    println()

    println("Header: Spline Linear Optimal\n")
    for(i in 10..80) {
        val nodes = NodeGenerator.getOptimalNodes(-5.0, 5.0, i)
        val properties = UserFunction.getNodes(nodes)
        val polys = Interpolator.interpolationSplineLinear(properties)
        TableGenerator.getTableLineByNodesSpline(properties, polys)

    }
    println()

    println("Header: Spline Square Standart\n")
    for(i in 10..60) {
        val nodes = NodeGenerator.getStandartNodes(-5.0, 5.0, i)
        val properties = UserFunction.getNodes(nodes)
        val polys = Interpolator.interpolationSplineSquare(properties)
        TableGenerator.getTableLineByNodesSpline(properties, polys)

    }
    println()

    println("Header: Spline Square Optimal\n")
    for(i in 10..60) {
        val nodes = NodeGenerator.getOptimalNodes(-5.0, 5.0, i)
        val properties = UserFunction.getNodes(nodes)
        val polys = Interpolator.interpolationSplineSquare(properties)
        TableGenerator.getTableLineByNodesSpline(properties, polys)

    }
    println()

    println("Header: Spline Cubic Standart\n")
    for(i in 10..80) {
        val nodes = NodeGenerator.getStandartNodes(-5.0, 5.0, i)
        val properties = UserFunction.getNodes(nodes)
        val polys = Interpolator.interpolationSplineCubic(properties)
        TableGenerator.getTableLineByNodesSpline(properties, polys)

    }
    println()

    println("Header: Spline Cubic Optimal\n")
    for(i in 10..80) {
        val nodes = NodeGenerator.getOptimalNodes(-5.0, 5.0, i)
        val properties = UserFunction.getNodes(nodes)
        val polys = Interpolator.interpolationSplineCubic(properties)
        TableGenerator.getTableLineByNodesSpline(properties, polys)

    }
    println()
}
