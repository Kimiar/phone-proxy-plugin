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

        private const val cmd_set_proxy = "adb shell settings put global http_proxy "
        private const val proxy_port = 8888

        private const val cmd_clear_proxy = "adb shell settings put global http_proxy :0"

        fun getLocalIp(): String {
            return InetAddress.getLocalHost().hostAddress
        }
    }

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
    }

    private fun getRandomNumber() = (1..100).random()

    fun setupProxyAddr(): String {
        val ipStr = NetworkUtil.getLocalIp() ?: return "no ip"
        execCommand(cmd_set_proxy + "${ipStr}:$proxy_port")
        return ipStr
    }

    fun clearProxy(): String {
        execCommand(cmd_clear_proxy)
        return "clear"
    }

    private fun getADBDevices(): String {
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
