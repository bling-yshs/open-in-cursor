package com.github.blingyshs.openincursor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import java.awt.Desktop
import java.net.URI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

class OpenInCursorAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

        log.info("触发了 Open In Cursor 插件")

        val project = e.project
        if (project == null) {
            @Suppress("DialogTitleCapitalization")
            Messages.showErrorDialog(
                "无法获取当前项目信息",
                "错误"
            )
            return
        }
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (virtualFile == null) {
            @Suppress("DialogTitleCapitalization")
            Messages.showErrorDialog(
                "无法获取当前文件信息",
                "错误"
            )
            return
        }
        // 获取项目根目录
        val projectPath = project.basePath
        // 获取当前文件路径
        val filePath = virtualFile.path

        // 使用设置中的配置
        val settings = OpenInCursorSettings.getInstance()
        val openInNewWindow = if (settings.openInNewWindow) "?windowId=_blank" else ""

        try {
            // 判断操作系统类型
            val isWindows = System.getProperty("os.name").lowercase().contains("windows")
            // Windows 系统需要在 file 后添加 /，其他系统不需要
            val filePrefix = if (isWindows) "file/" else "file"

            // 使用配置的 IDE 名称打开项目根目录
            val projectUrl = "${settings.ideName}://$filePrefix$projectPath$openInNewWindow"
            Desktop.getDesktop().browse(URI(projectUrl))

            // 使用配置的延迟时间
            CompletableFuture.delayedExecutor(settings.delaySeconds.toLong(), TimeUnit.SECONDS)
                .execute {
                    try {
                        // 获取当前编辑器
                        val editor = e.getData(CommonDataKeys.EDITOR)
                        // 获取当前行号
                        val lineNumber = editor?.caretModel?.logicalPosition?.line
                        // 打开文件
                        val fileUrl = "${settings.ideName}://$filePrefix$filePath:${lineNumber?.plus(1) ?: 1}"
                        log.info("打开文件: $fileUrl")
                        Desktop.getDesktop().browse(URI(fileUrl))

                    } catch (ex: Exception) {
                        log.error("打开文件失败: " + ex.message)
                    }
                }
        } catch (ex: Exception) {
            log.error("打开 Cursor 失败: " + ex.message)
        }
    }
}
