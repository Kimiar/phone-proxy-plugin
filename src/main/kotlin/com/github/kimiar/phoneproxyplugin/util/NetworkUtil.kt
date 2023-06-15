package com.github.kimiar.phoneproxyplugin.util

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

object NetworkUtil {


    fun getLocalIp(): String? {
        // todo 排除无网情况下，获取到 ipv6 的问题
        return kotlin.runCatching {
            Inet4Address.getLocalHost().hostAddress
        }.getOrNull()
    }

    fun getLocalHostExactAddress(): InetAddress? {
        try {
            var candidateAddress: InetAddress? = null

            val networkInterfaces = NetworkInterface.getNetworkInterfaces() ?: return null
            while (networkInterfaces.hasMoreElements()) {
                val iface = networkInterfaces.nextElement()
                // 该网卡接口下的ip会有多个，也需要一个个的遍历，找到自己所需要的
                val inetAdders = iface.inetAddresses
                while ( inetAdders.hasMoreElements()) {
                    val inetAddr = inetAdders.nextElement();
                    // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                    if (!inetAddr.isLoopbackAddress) {
                        if (inetAddr.isSiteLocalAddress) {
                            // 如果是site-local地址，就是它了 就是我们要找的
                            // ~~~~~~~~~~~~~绝大部分情况下都会在此处返回你的ip地址值~~~~~~~~~~~~~
                            return inetAddr;
                        }

                        // 若不是site-local地址 那就记录下该地址当作候选
                        if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }

                    }
                }
            }

            // 如果出去loopback回环地之外无其它地址了，那就回退到原始方案吧
            return candidateAddress ?: InetAddress.getLocalHost();
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return null
    }
}