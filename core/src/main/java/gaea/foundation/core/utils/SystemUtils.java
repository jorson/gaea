package gaea.foundation.core.utils;

import gaea.foundation.core.utils.jvm.JVMExitSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

public class SystemUtils {
    private static Log logger = LogFactory.getLog(SystemUtils.class);

    private SystemUtils() {
    }

    /**
     * 环回地址
     */
    private static final String LOOPBACK_IPADDRESS = "127.0.0.1";

    /**
     * 退出Java程序
     *
     * @param status 状态码，非0的状态码表示异常终止
     */
    public static final void exit(int status) {
        JVMExitSupport.exit(status);
    }

    /**
     * 强行退出Java程序
     * <p/>
     * 该方法会直接退出，不管程序中是否还有线程在处理
     *
     * @param status 状态码，非0的状态码表示异常终止
     */
    public static final void runExit(int status) {
        JVMExitSupport.exit(status);
    }

    /**
     * 获取本地主机上绑定的所有IP地址
     *
     * @return 本地主机上绑定的所有IP地址
     */
    public static final List<String> getLocalIps() {
        List<String> ips = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses != null && addresses.hasMoreElements()) {
                    InetAddress addresse = addresses.nextElement();
                    String ip = addresse.getHostAddress();
                    if (!LOOPBACK_IPADDRESS.equals(ip)) {
                        ips.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            logger.warn("get all local ipaddress happen error", e);
        }
        return ips;
    }

    /**
     * 取得当前服务器的IP地址
     *
     * @return
     */
    public static String getLocalHostIp() {
        String ipAddress = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            byte[] ipAddr = addr.getAddress();
            int i = 0;
            for (; i < ipAddr.length - 1; i++) {
                ipAddress += ipAddr[i] & 0xFF;
                ipAddress += ".";
            }
            ipAddress += ipAddr[i] & 0xFF;
        } catch (UnknownHostException e) {
            logger.warn("get local ipaddress happen error", e);
        }

        return ipAddress;
    }

    /**
     * 取得当前服务器的名称
     *
     * @return
     */
    public static String getLocalHostName() {
        String hostname = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
            logger.warn("get local hostname happen error", e);
        }
        return hostname;
    }

    public static void main(String args[]) {
        System.out.println(getLocalIps().toArray()[0]);
        System.out.println(getLocalHostIp());
        System.out.println(getLocalHostName());
    }

}
