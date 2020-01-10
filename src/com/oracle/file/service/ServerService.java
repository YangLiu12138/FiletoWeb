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
 * �������߼�����
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

	// ����������
	// ���������������߳�
	public void startServer() {
		try {
			ServerSocket ss = new ServerSocket(ServerMsg.port);
			JOptionPane.showMessageDialog(null, "�������Ѿ��ɹ�����");
			// �ɹ�����
			btnStart.setVisible(false);
			btnStop.setVisible(true);
			btnFileSend.setVisible(true);
			while (true) {
				// �ȴ��ͻ�������
				Socket clientScoket = ss.accept();

				System.out.println("�Ѿ��пͻ���������");
				// �Ѿ��пͻ���������
				ServerMsg.list.add(clientScoket);
				System.out.println("�����е�socket����Ϊ:" + ServerMsg.list.size());
				// ����һ���̣߳����ӵ�ǰsocket�ͻ���
				new RefreshPanelThread(panel, clientScoket).start();
				// ����һ����ȡ���ݵ��̣߳����ڼ��ͻ����Ƿ�Ͽ���
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							while (true) {
								System.out.println("������");
								int b = clientScoket.getInputStream().read();
								System.out.println("������" + b);

								if (b == -1) {
									// System.out.println("�ͻ��˶Ͽ��� ");
									// ͨ��ip��ַɾ��
									Component comps[] = panel.getComponents();
									for (Component component : comps) {
										if (component instanceof JPanel) {
											JPanel j = (JPanel) component;
											TitledBorder lb = (TitledBorder) j.getBorder();
											String ip = lb.getTitle();
											String cip = clientScoket.getInetAddress().getHostAddress();
											if (ip.equals(cip)) {// �ҵ��˿ͻ���panel
												panel.remove(j);
											}
										}
									}

									break;
								}

							}
						} catch (IOException e) {
							System.out.println("�ͻ��˶Ͽ��� ======" + e.getMessage());
							// ͨ��ip��ַɾ��
							Component comps[] = panel.getComponents();
							for (Component component : comps) {
								if (component instanceof JPanel) {
									JPanel j = (JPanel) component;
									TitledBorder lb = (TitledBorder) j.getBorder();
									String ip = lb.getTitle();
									String cip = clientScoket.getInetAddress().getHostAddress();
									if (ip.equals(cip)) {// �ҵ��˿ͻ���panel
										panel.remove(j);
									}
								}
							}
						}

					}
				}).start();

			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "��������:" + e.getMessage());
		} catch (Exception e) {
			System.out.println("�ͻ���������--->>>>>>" + e.getMessage());
		}
	}

	@Override
	public void run() {

		startServer();

	}

}
