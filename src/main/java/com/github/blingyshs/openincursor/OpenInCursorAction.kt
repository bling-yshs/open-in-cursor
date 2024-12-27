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
            Messages.showErrorDialog(
                "无法获取当前项目信息",
                "错误"
            )
            return
        }
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (virtualFile == null) {
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
        val openInNewWindow = "?windowId=_blank"

        try {
            // 首先打开项目根目录
            val projectUrl = "cursor://file/$projectPath$openInNewWindow"
            Desktop.getDesktop().browse(URI(projectUrl))

            // 使用配置的延迟时间
            val settings = OpenInCursorSettings.getInstance()
            // 延迟打开文件
            CompletableFuture.delayedExecutor(settings.delaySeconds.toLong(), TimeUnit.SECONDS)
                .execute {
                    try {
                        // 获取当前编辑器
                        val editor = e.getData(CommonDataKeys.EDITOR)
                        // 获取当前行号
                        val lineNumber = editor?.caretModel?.logicalPosition?.line
                        // 打开文件
                        val fileUrl = "cursor://file/$filePath:${lineNumber?.plus(1) ?: 1}"
                        System.out.println("打开文件: $fileUrl")
                        log.info("打开文件: $fileUrl")
                        Desktop.getDesktop().browse(URI(fileUrl))

                    } catch (ex: Exception) {
                        log.error("打开文件失败: " + ex.message)
                        System.err.println("打开文件失败: " + ex.message)
                    }
                }
        } catch (ex: Exception) {
            log.error("打开 Cursor 失败: " + ex.message)
            System.err.println("打开 Cursor 失败: " + ex.message)
        }
    }
}
