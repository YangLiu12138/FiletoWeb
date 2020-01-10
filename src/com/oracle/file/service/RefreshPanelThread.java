package com.oracle.file.service;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.oracle.file.entity.ServerMsg;

/**
 * 刷新客户端界面线程
 * 
 * @author Administrator
 *
 */
public class RefreshPanelThread extends Thread {

	private JPanel panel;
	private Socket socket;

	public RefreshPanelThread(JPanel panel, Socket clientScoket) {
		this.panel = panel;
		this.socket = clientScoket;
	}

	// 有一个线程读取客户端连接的信息
	// 因为ui一个线程，读取一个线程，不干扰

	@Override
	public void run() {
		System.out.println("刷新界面线程已成功启动");

		int count = 30;
		int row = 1, col = 6;// 表格的行，列
		if (count <= 8) {
			row = 1;
			col = count;
		} else if (count > 8 && count <= 16) {
			row = 2;
			col = 8;
		} else if (count > 16 && count <= 24) {
			row = 3;
			col = 8;
		} else {
			row = 4;
			col = 8;
		}

		panel.setLayout(new GridLayout(row, col, 0, 0));

		InetAddress add = socket.getInetAddress();
		String ip = add.getHostAddress();
		String name = add.getHostName();

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), ip, TitledBorder.LEADING,
				TitledBorder.TOP, null, Color.RED));
		panel_1.setLayout(new GridLayout(3, 1, 0, 0));
		panel_1.add(new JLabel(name));
		// panel_1.add(new JLabel("10:52:36"));
		panel.add(panel_1);
		panel_1.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// 获取到事件源，谁触发的
				JPanel p = (JPanel) e.getSource();

				if (p.getBackground() == Color.ORANGE) {
					p.setBackground(null);
				} else {
					p.setBackground(Color.ORANGE);
				}

			}
		});
		System.out.println("界面:" + ip + "成功创建");
	}

}
