package com.oracle.file.service;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.oracle.file.entity.ServerMsg;

/**
 * 服务器逻辑处理
 * 
 * @author Administrator
 *
 */
public class ServerService implements Runnable {

	private JButton btnStart;
	private JButton btnStop;
	private JButton btnFileSend;
	private JPanel panel;

	public ServerService(JButton btnStart, JButton btnStop, JButton btnFileSend, JPanel panel) {
		this.btnStart = btnStart;
		this.btnStop = btnStop;
		this.btnFileSend = btnFileSend;
		this.panel = panel;
	}

	// 启动服务器
	// 还不能阻塞其他线程
	public void startServer() {
		try {
			ServerSocket ss = new ServerSocket(ServerMsg.port);
			JOptionPane.showMessageDialog(null, "服务器已经成功启动");
			// 成功启动
			btnStart.setVisible(false);
			btnStop.setVisible(true);
			btnFileSend.setVisible(true);
			while (true) {
				// 等待客户端连接
				Socket clientScoket = ss.accept();

				System.out.println("已经有客户端上线了");
				// 已经有客户端上线了
				ServerMsg.list.add(clientScoket);
				System.out.println("集合中的socket个数为:" + ServerMsg.list.size());
				// 开启一个线程，增加当前socket客户端
				new RefreshPanelThread(panel, clientScoket).start();
				// 开启一个读取数据的线程，用于检测客户端是否断开了
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							while (true) {
								System.out.println("嘟嘟嘟嘟嘟嘟");
								int b = clientScoket.getInputStream().read();
								System.out.println("嘟嘟嘟嘟嘟嘟" + b);

								if (b == -1) {
									// System.out.println("客户端断开了 ");
									// 通过ip地址删除
									Component comps[] = panel.getComponents();
									for (Component component : comps) {
										if (component instanceof JPanel) {
											JPanel j = (JPanel) component;
											TitledBorder lb = (TitledBorder) j.getBorder();
											String ip = lb.getTitle();
											String cip = clientScoket.getInetAddress().getHostAddress();
											if (ip.equals(cip)) {// 找到了客户端panel
												panel.remove(j);
											}
										}
									}

									break;
								}

							}
						} catch (IOException e) {
							System.out.println("客户端断开了 ======" + e.getMessage());
							// 通过ip地址删除
							Component comps[] = panel.getComponents();
							for (Component component : comps) {
								if (component instanceof JPanel) {
									JPanel j = (JPanel) component;
									TitledBorder lb = (TitledBorder) j.getBorder();
									String ip = lb.getTitle();
									String cip = clientScoket.getInetAddress().getHostAddress();
									if (ip.equals(cip)) {// 找到了客户端panel
										panel.remove(j);
									}
								}
							}
						}

					}
				}).start();

			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "启动错误:" + e.getMessage());
		} catch (Exception e) {
			System.out.println("客户端下线了--->>>>>>" + e.getMessage());
		}
	}

	@Override
	public void run() {

		startServer();

	}

}
