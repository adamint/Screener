package com.adamratzman.screener.main

import javafx.concurrent.Task
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.system.exitProcess

class ScreenerWindow(width: Double, height: Double, primaryStage: Stage) : Stage() {
    var startX: Double = 0.0
    var startY: Double = 0.0
    var posX: Double = 0.0
    var posY: Double = 0.0
    val canvas = Canvas()
    val graphicsContext = canvas.graphicsContext2D
    var foreground = Color.CRIMSON
    var background = Color.rgb(0, 0, 0, 0.3)
    var screenerHome: String = if (System.getProperty("os.name").startsWith("Windows", true)) {
        System.getProperty("user.home") + "/Screener"
    } else "~/Screener"
    val screenerHomeFile = File(screenerHome)

    init {
        if (!screenerHomeFile.exists()) {
            val success = screenerHomeFile.mkdir()
            if (!success) {
                println("Unable to create screener folder")
                exitProcess(1)
            }
            File("$screenerHome/Screenshots").mkdir()
            File("$screenerHome/Settings").mkdir()
        }
        initOwner(primaryStage)

        x = 0.0
        y = 0.0
        this.width = width
        this.height = height

        initStyle(StageStyle.TRANSPARENT)
        isAlwaysOnTop = true

        canvas.width = width
        canvas.height = height

        graphicsContext.setLineDashes(6.0);
        graphicsContext.font = Font.font("null", FontWeight.BOLD, 14.0);


        val borderPane = BorderPane()
        borderPane.style = "-fx-background-color:rgb(0,0,0,0.1);";
        borderPane.center = canvas

        scene = Scene(borderPane, Color.TRANSPARENT)
        scene.cursor = Cursor.CROSSHAIR
        scene.onKeyPressed = EventHandler {
            // pressing S gives full screen image
            if (it.code == KeyCode.S) {
                capture(0.0, 0.0, width, height)
            }
            exitProcess(0)
        }


        canvas.onMousePressed = EventHandler {
            startX = it.screenX
            startY = it.screenY
        }

        canvas.onMouseDragged = EventHandler {
            posX = it.x
            posY = it.y
            paint()
        }

        canvas.onMouseReleased = EventHandler {
            if (Math.abs(posX) >= 1.0 && Math.abs(posY) >= 1.0) {
                capture(startX, startY, posX - startX, posY - startY)
            } else exitProcess(0)
        }
    }

    private fun paint() {
        graphicsContext.clearRect(0.0, 0.0, width, height)
        graphicsContext.stroke = foreground
        graphicsContext.fill = background
        graphicsContext.lineWidth = 3.0

        graphicsContext.strokeRect(startX, startY, posX - startX, posY - startY)
    }

    private fun capture(x: Double, y: Double, width: Double, height: Double) {
        val task = object : Task<Unit>() {
            override fun call() {
                graphicsContext.clearRect(0.0, 0.0, this@ScreenerWindow.width, this@ScreenerWindow.height)
            }
        }
        task.setOnSucceeded {
            Thread {
                val robot = Robot()
                val image = robot.createScreenCapture(Rectangle(x.toInt(), y.toInt(), width.toInt(), height.toInt()))
                val imageName = Date().toLocaleString().replace(":", " ")
                ImageIO.write(image, "jpg", File("$screenerHome/Screenshots", "$imageName.jpg"))
                System.exit(0)
            }.start()
        }
        Thread(task).start()
    }
}