package test;

import java.util.*;
import java.net.*;

public class SubnetMask {
    public static void main(String[] args) {
        InetAddress ip = null;
        NetworkInterface ni = null;
        try {
            ip = InetAddress.getLocalHost();
            ni = NetworkInterface.getByInetAddress(ip);// 搜索绑定了指定IP地址的网络接口
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<InterfaceAddress> list = ni.getInterfaceAddresses();// 获取此网络接口的全部或部分
                                                                    // InterfaceAddresses
                                                                    // 所组成的列表
        if (list.size() > 0) {
//            int mask = list.get(0).getNetworkPrefixLength(); // 子网掩码的二进制1的个数
            int mask = 32; // 子网掩码的二进制1的个数
            System.out.println(mask);
            StringBuilder maskStr = new StringBuilder();
            int[] maskIp = new int[4];
            for (int i = 0; i < maskIp.length; i++) {
                maskIp[i] = (mask >= 8) ? 255 : (mask > 0 ? (mask & 0xff) : 0);
                mask -= 8;
                maskStr.append(maskIp[i]);
                if (i < maskIp.length - 1) {
                    maskStr.append(".");
                }
            }
            System.out.println("SubnetMask:" + maskStr);
        }
    }
}