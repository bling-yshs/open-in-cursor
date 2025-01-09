package com.github.blingyshs.openincursor

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.blingyshs.openincursor.OpenInCursorSettings",
    storages = [Storage("OpenInCursorSettings.xml")]
)
class OpenInCursorSettings : PersistentStateComponent<OpenInCursorSettings> {
    var delaySeconds: Int = 3
    var openInNewWindow: Boolean = true
    var ideName: String = "cursor"

    override fun getState(): OpenInCursorSettings = this

    override fun loadState(state: OpenInCursorSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): OpenInCursorSettings = service()
    }
}
