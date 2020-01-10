package com.oracle.file.frame;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.TitledBorder;

import com.oracle.file.entity.ServerMsg;
import com.oracle.file.service.ServerService;
import com.oracle.util.DateUtil;
import com.sun.corba.se.spi.orbutil.fsm.Input;

import java.awt.Color;
import java.awt.Component;

import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TeacherFrame extends JFrame {
	private JLabel lblTime;
	private JButton btnFileSend;
	private JButton btnStop;
	private JButton btnStart;
	// �ͻ�����Ϣ��������
	JPanel panel = new JPanel();

	/**
	 * ���췽�������ǵ��ഴ����ʱ�򣬾�Ҫ��ɵ�һϵ�г�ʼ������ ��֮Ϊ���췽��
	 */
	public TeacherFrame() {
		setTitle("JAVA Socket Thread \u5E94\u7528");
		setSize(503, 400);

		JToolBar toolBar = new JToolBar();
		{
			JButton btn = new JButton("����������");
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ��ʾ���������ý���
					new ServerSetFrame().setVisible(true);

				}
			});
			toolBar.add(btn);
		}

		{
			btnStart = new JButton("����������");
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnStart.setEnabled(false);
					ServerService ss = new ServerService(btnStart, btnStop, btnFileSend, panel);
					// �߳�����
					new Thread(ss).start();
				}
			});
			toolBar.add(btnStart);
		}

		{
			btnStop = new JButton("ֹͣ������");
			toolBar.add(btnStop);
			btnStop.setVisible(false);
		}
		{
			btnFileSend = new JButton("�ļ�����");
			btnFileSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// �ȵ����ļ�ѡ��Ի���
					List<String> ips = new ArrayList<String>();
					List<JProgressBar> pplist = new ArrayList<JProgressBar>();

					JFileChooser jfc = new JFileChooser();
					int result = jfc.showOpenDialog(null);
					if (result == 0) {
						// ѡ����ļ�
						File f = jfc.getSelectedFile();
						// �ж���Щ�ͻ��˱�ѡ����
						Component comp[] = panel.getComponents();
						for (Component component : comp) {
							if (component instanceof JPanel) {
								JPanel p = (JPanel) component;
								// ����ɫ�� ��ɫ������ѡ�е�
								if (p.getBackground() == Color.ORANGE) {

									TitledBorder tb = (TitledBorder) p.getBorder();
									String ip = tb.getTitle();
									ips.add(ip);
								}

								JProgressBar pb;
								if(p.getComponents().length==2) {
									  pb=  (JProgressBar) p.getComponent(1);
									pb.setValue(0);
								}else {
									//��̬����������	
									pb=new JProgressBar();
									p.add(pb);
									
									
								}
								pplist.add(pb);
								//System.out.println();


							}
						}

						// ����Щ�ͻ��˷�������
						List<Socket> list = ServerMsg.list;
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								
								int index=0;//���ϵ��±�
								for (String ip : ips) {
									for (Socket socket : list) {
										String sip = socket.getInetAddress().getHostAddress();
										if (ip.equals(sip)) {// ��ѡ��Ŀͻ��˵�socket��ȡ��
											System.out.println("������"+ip+"�����µ��ļ�-->"+f.getName());	
											try {
												OutputStream os = socket.getOutputStream();
												System.out.println("pplist-->"+pplist.size());
												System.out.println("index-->"+index);
												JProgressBar jpb= pplist.get(index);
												jpb.setMaximum((int)f.length());
												System.out.println("JProgressBar���ֵΪ:"+f.length());
												// ObjectOutputStream ois = new ObjectOutputStream(os);
												// ois.writeObject(f);
												//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
												
												
												byte tzdatas[]=new byte[400];
												//ʹ�ù̶����ȸ�֪�ͻ����ļ���С������
												String tzstr = f.length() + ","+f.getName();
												byte ss[]= tzstr.getBytes();
												for (int i = 0; i < ss.length; i++) {
													tzdatas[i]=ss[i];
												}
 												os.write(tzdatas);
												System.out.println("�ɹ�֪ͨ�ͻ���-->"+(f.length() + ","+f.getName()+"\r\n"));
												//bw.newLine();// дһ�����ݳ�ȥ
											
												//ѭ����ͻ��˷����ļ�����
												
												
												InputStream socketIN= socket.getInputStream() ;
												// ��ȡ�����ļ��Ĵ�С
												int ans =socketIN.read();
												System.out.println("�������յ��ͻ��˷�����-->"+ans);
												
												InputStream is =new FileInputStream(f);
												byte datas[]=new byte[1024*5];
												int len ;
												int tlen=0;
												while((len=is.read(datas)) !=-1 ) {
													os.write(datas,0,len);
													tlen+=len;
													System.out.println("���ڴ���-->"+tlen);
													jpb.setValue(tlen);
												}
												
												is.close();
												

												System.out.println("�Ѿ��ɹ���-->" + sip + "�����ļ�");
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											
											//listprocess ���ǲ����� processbar
											//pplist.remove(index);
											//��processbarҲɾ��
											
											
											index++;//�ж���һ��ip��
										}
									}
									
								}
								
								
							}
						}).start();

					}

				}
			});
			toolBar.add(btnFileSend);
			btnFileSend.setVisible(false);
		}

		getContentPane().add(toolBar, BorderLayout.NORTH);
		{
			JPanel panel = new JPanel();
			getContentPane().add(panel, BorderLayout.SOUTH);
			{
				lblTime = new JLabel("New label");
				panel.add(lblTime);
			}
		}
		{
			// �м�������壬��������
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);

			scrollPane.setViewportView(panel);

		}
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLableTime();
	}

	/**
	 * ����ʱ��
	 */
	public void setLableTime() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					lblTime.setText(DateUtil.getDate(DateUtil.DATE1));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();

	}
}
