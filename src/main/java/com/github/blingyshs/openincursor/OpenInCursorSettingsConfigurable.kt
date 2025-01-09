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
import javax.swing.JTextField

class OpenInCursorSettingsConfigurable : Configurable {
    private var settingsComponent: OpenInCursorSettingsComponent? = null

    override fun getDisplayName(): String = "Open In Cursor"

    override fun createComponent(): JComponent {
        settingsComponent = OpenInCursorSettingsComponent()
        return settingsComponent!!.panel
    }

    override fun isModified(): Boolean {
        val settings = OpenInCursorSettings.getInstance()
        return settingsComponent!!.delaySeconds != settings.delaySeconds ||
               settingsComponent!!.openInNewWindow != settings.openInNewWindow ||
               settingsComponent!!.ideName != settings.ideName
    }

    override fun apply() {
        val settings = OpenInCursorSettings.getInstance()
        settings.delaySeconds = settingsComponent!!.delaySeconds
        settings.openInNewWindow = settingsComponent!!.openInNewWindow
        settings.ideName = settingsComponent!!.ideName
    }

    override fun reset() {
        val settings = OpenInCursorSettings.getInstance()
        settingsComponent!!.delaySeconds = settings.delaySeconds
        settingsComponent!!.openInNewWindow = settings.openInNewWindow
        settingsComponent!!.ideName = settings.ideName
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}

class OpenInCursorSettingsComponent {
    val panel: JPanel = JPanel()
    private val delaySpinner: JSpinner
    private val newWindowCheckBox: JCheckBox
    private val ideNameField: JTextField  // 新增

    init {
        panel.layout = FlowLayout(FlowLayout.LEFT)
        
        // 创建一个垂直布局的面板
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)

        // ide 名称设置
        val ideNamePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        ideNamePanel.add(JLabel("IDE名称:"))
        ideNameField = JTextField(10)
        ideNamePanel.add(ideNameField)
        mainPanel.add(ideNamePanel)

        // 延迟设置
        val delayPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        delayPanel.add(JLabel("打开文件延迟时间（秒）:"))
        delaySpinner = JSpinner(SpinnerNumberModel(2, 1, 10, 1))
        delayPanel.add(delaySpinner)
        mainPanel.add(delayPanel)
        
        // 新窗口选项
        val newWindowPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        newWindowCheckBox = JCheckBox("在新窗口中打开当前项目")
        newWindowPanel.add(newWindowCheckBox)
        mainPanel.add(newWindowPanel)

        panel.add(mainPanel)
    }

    var ideName: String
        get() = ideNameField.text
        set(value) {
            ideNameField.text = value
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
