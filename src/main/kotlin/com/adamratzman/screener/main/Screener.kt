package com.adamratzman.screener.main

import javafx.application.Application
import javafx.stage.Screen
import javafx.stage.Stage

class Screener : Application() {
    override fun start(primaryStage: Stage) {
        val window = ScreenerWindow(Screen.getPrimary().bounds.width, Screen.getPrimary().bounds.height, primaryStage)
        window.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Screener::class.java)
        }
    }
}
