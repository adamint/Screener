package com.adamratzman.screener.settings

import javafx.stage.Stage

abstract class SavePrompt(stage: Stage, val inputs: List<PromptInput>) {

    fun render(vararg additionalInputs: PromptInput) {

    }

    abstract fun onClick(): HashMap<String, String>
}

abstract class ScreenshotSavePrompt(stage: Stage,inputs: List<PromptInput>) : SavePrompt(stage,inputs) {
    val fileName: PromptInput = PromptInput("Screenshot name", null, "Screenshot @ MM-dd-yyyy HH:mm:ss")

    fun render() = super.render(fileName)
}

data class PromptInput(val name: String, val hint: String?, val default: String?)
