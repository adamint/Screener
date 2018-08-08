package com.adamratzman.screener.settings.locations

enum class ScreenshotLocations(val readable: String) {
    IMGUR("Imgur");

    override fun toString() = readable
}