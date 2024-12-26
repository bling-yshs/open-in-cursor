package com.github.blingyshs.openincursor

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import java.awt.FlowLayout

class OpenInCursorSettingsConfigurable : Configurable {
    private var settingsComponent: OpenInCursorSettingsComponent? = null

    override fun getDisplayName(): String = "Open in Cursor"

    override fun createComponent(): JComponent {
        settingsComponent = OpenInCursorSettingsComponent()
        return settingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = OpenInCursorSettings.getInstance()
        return settingsComponent!!.delaySeconds != settings.delaySeconds
    }

    override fun apply() {
        val settings = OpenInCursorSettings.getInstance()
        settings.delaySeconds = settingsComponent!!.delaySeconds
    }

    override fun reset() {
        val settings = OpenInCursorSettings.getInstance()
        settingsComponent!!.delaySeconds = settings.delaySeconds
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}

class OpenInCursorSettingsComponent {
    val panel: JPanel = JPanel(FlowLayout(FlowLayout.LEFT))
    private val delaySpinner: JSpinner

    init {
        panel.add(JLabel("打开文件延迟时间（秒）:"))
        delaySpinner = JSpinner(SpinnerNumberModel(2, 1, 10, 1))
        panel.add(delaySpinner)
    }

    var delaySeconds: Int
        get() = delaySpinner.value as Int
        set(value) {
            delaySpinner.value = value
        }
}
