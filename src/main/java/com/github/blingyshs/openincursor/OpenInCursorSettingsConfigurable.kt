package com.github.blingyshs.openincursor

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JLabel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import java.awt.FlowLayout
import javax.swing.JCheckBox
import javax.swing.BoxLayout

class OpenInCursorSettingsConfigurable : Configurable {
    private var settingsComponent: OpenInCursorSettingsComponent? = null

    override fun getDisplayName(): String = "Open in Cursor"

    override fun createComponent(): JComponent {
        settingsComponent = OpenInCursorSettingsComponent()
        return settingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = OpenInCursorSettings.getInstance()
        return settingsComponent!!.delaySeconds != settings.delaySeconds ||
               settingsComponent!!.openInNewWindow != settings.openInNewWindow
    }

    override fun apply() {
        val settings = OpenInCursorSettings.getInstance()
        settings.delaySeconds = settingsComponent!!.delaySeconds
        settings.openInNewWindow = settingsComponent!!.openInNewWindow
    }

    override fun reset() {
        val settings = OpenInCursorSettings.getInstance()
        settingsComponent!!.delaySeconds = settings.delaySeconds
        settingsComponent!!.openInNewWindow = settings.openInNewWindow
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}

class OpenInCursorSettingsComponent {
    val panel: JPanel = JPanel()
    private val delaySpinner: JSpinner
    private val newWindowCheckBox: JCheckBox

    init {
        panel.layout = FlowLayout(FlowLayout.LEFT)
        
        // 创建一个垂直布局的面板
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        
        // 第一行：延迟设置
        val delayPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        delayPanel.add(JLabel("打开文件延迟时间（秒）:"))
        delaySpinner = JSpinner(SpinnerNumberModel(2, 1, 10, 1))
        delayPanel.add(delaySpinner)
        mainPanel.add(delayPanel)
        
        // 第二行：新窗口选项
        val newWindowPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        newWindowCheckBox = JCheckBox("在 Cursor 的新窗口中打开当前项目")
        newWindowPanel.add(newWindowCheckBox)
        mainPanel.add(newWindowPanel)
        
        panel.add(mainPanel)
    }

    var delaySeconds: Int
        get() = delaySpinner.value as Int
        set(value) {
            delaySpinner.value = value
        }

    var openInNewWindow: Boolean
        get() = newWindowCheckBox.isSelected
        set(value) {
            newWindowCheckBox.isSelected = value
        }
}
