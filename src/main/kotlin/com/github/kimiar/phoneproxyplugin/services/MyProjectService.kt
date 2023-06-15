package com.github.kimiar.phoneproxyplugin.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.github.kimiar.phoneproxyplugin.MyBundle
import com.github.kimiar.phoneproxyplugin.util.NetworkUtil
import com.intellij.openapi.command.executeCommand
import com.intellij.util.net.loopbackSocketAddress
import java.net.InetAddress

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {

    companion object {

        private const val cmd_devices = "adb devices"

        fun getLocalIp(): String {
            return InetAddress.getLocalHost().hostAddress
        }
    }

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
    }

    fun getRandomNumber() = (1..100).random()

    fun getADBDevices(): String {
        return NetworkUtil.getLocalIp() ?: "no ip"
    }

    private fun execCommand(cmdStr: String): String {
        val resultStr =  Runtime.getRuntime().exec(cmdStr).inputStream.use {
            String(it.readBytes())
        }

        thisLogger().info("command:$cmdStr -> result:$resultStr")
        return resultStr
    }




    private fun testSome() {
        executeCommand {

        }
    }
}

fun main() {
    println(MyProjectService.getLocalIp())
}
