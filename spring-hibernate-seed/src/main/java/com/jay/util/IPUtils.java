package com.jay.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 
 * @author Jay Zhang
 * 
 */
public class IPUtils {

	private static String SERVER_IP = null;

	public static String getServerIP() {
		if (SERVER_IP != null)
			return SERVER_IP;

		try {
			Enumeration<InetAddress> ips = null;
			InetAddress ip = null;
			Enumeration<NetworkInterface> networks = NetworkInterface
					.getNetworkInterfaces();
			while (networks.hasMoreElements()) {
				ips = networks.nextElement().getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = ips.nextElement();
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& !ip.getHostAddress().contains(":")) {
						SERVER_IP = ip.getHostAddress();
						return SERVER_IP;
					}
				}
			}
		} catch (Exception e) {
		}

		SERVER_IP = "";

		return SERVER_IP;
	}

	public static String getLocalIP() {
		try {
			byte[] ip = InetAddress.getLocalHost().getAddress();
			StringBuilder rs = new StringBuilder();
			int len = ip.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					rs.append(".");
				}
				rs.append(ip[i] & 0xFF);
			}
			return rs.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return "";
	}
}
