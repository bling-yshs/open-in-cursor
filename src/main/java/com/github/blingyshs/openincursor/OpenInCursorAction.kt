package com.github.blingyshs.openincursor

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import java.awt.Desktop
import java.net.URI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import com.intellij.openapi.ui.Messages

class OpenInCursorAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val project = e.project
        if (project == null) {
            Messages.showErrorDialog(
                "无法获取当前项目信息",
                "错误"
            )
            return
        }
        if (virtualFile == null) {
            Messages.showErrorDialog(
                "无法获取当前文件信息",
                "错误"
            )
            return
        }
        val projectPath = project.basePath
        val filePath = virtualFile.path

        try {
            // 首先打开项目根目录
            val projectUrl = "cursor://file/$projectPath"
            Desktop.getDesktop().browse(URI(projectUrl))

            // 3秒后打开选中的文件
            CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS)
                .execute {
                    try {
                        val fileUrl = "cursor://file/$filePath"
                        Desktop.getDesktop().browse(URI(fileUrl))
                    } catch (ex: Exception) {
                        System.err.println("打开文件失败: " + ex.message)
                    }
                }
        } catch (ex: Exception) {
            System.err.println("打开 Cursor 失败: " + ex.message)
        }
    }
}
