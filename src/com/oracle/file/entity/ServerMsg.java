package com.oracle.file.entity;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器信息类，数据储存的
 * 
 * @author Administrator
 *
 */
public class ServerMsg {

	// 默认端口号
	public static int port = 8888;
	// 所有客户端信息
	public static List<Socket> list = new ArrayList<Socket>();

}
