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
	// 客户端信息界面容器
	JPanel panel = new JPanel();

	/**
	 * 构造方法，就是当类创建的时候，就要完成的一系列初始化任务 称之为构造方法
	 */
	public TeacherFrame() {
		setTitle("JAVA Socket Thread \u5E94\u7528");
		setSize(503, 400);

		JToolBar toolBar = new JToolBar();
		{
			JButton btn = new JButton("服务器设置");
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 显示服务器设置界面
					new ServerSetFrame().setVisible(true);

				}
			});
			toolBar.add(btn);
		}

		{
			btnStart = new JButton("启动服务器");
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btnStart.setEnabled(false);
					ServerService ss = new ServerService(btnStart, btnStop, btnFileSend, panel);
					// 线程启动
					new Thread(ss).start();
				}
			});
			toolBar.add(btnStart);
		}

		{
			btnStop = new JButton("停止服务器");
			toolBar.add(btnStop);
			btnStop.setVisible(false);
		}
		{
			btnFileSend = new JButton("文件传输");
			btnFileSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 先弹出文件选择对话框
					List<String> ips = new ArrayList<String>();
					List<JProgressBar> pplist = new ArrayList<JProgressBar>();

					JFileChooser jfc = new JFileChooser();
					int result = jfc.showOpenDialog(null);
					if (result == 0) {
						// 选择的文件
						File f = jfc.getSelectedFile();
						// 判断哪些客户端被选择了
						Component comp[] = panel.getComponents();
						for (Component component : comp) {
							if (component instanceof JPanel) {
								JPanel p = (JPanel) component;
								// 背景色是 橙色，才是选中的
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
									//动态创建进度条	
									pb=new JProgressBar();
									p.add(pb);
									
									
								}
								pplist.add(pb);
								//System.out.println();


							}
						}

						// 向这些客户端发送数据
						List<Socket> list = ServerMsg.list;
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								
								int index=0;//集合的下标
								for (String ip : ips) {
									for (Socket socket : list) {
										String sip = socket.getInetAddress().getHostAddress();
										if (ip.equals(sip)) {// 将选择的客户端的socket获取到
											System.out.println("即将给"+ip+"传输新的文件-->"+f.getName());	
											try {
												OutputStream os = socket.getOutputStream();
												System.out.println("pplist-->"+pplist.size());
												System.out.println("index-->"+index);
												JProgressBar jpb= pplist.get(index);
												jpb.setMaximum((int)f.length());
												System.out.println("JProgressBar最大值为:"+f.length());
												// ObjectOutputStream ois = new ObjectOutputStream(os);
												// ois.writeObject(f);
												//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
												
												
												byte tzdatas[]=new byte[400];
												//使用固定长度告知客户端文件大小和名称
												String tzstr = f.length() + ","+f.getName();
												byte ss[]= tzstr.getBytes();
												for (int i = 0; i < ss.length; i++) {
													tzdatas[i]=ss[i];
												}
 												os.write(tzdatas);
												System.out.println("成功通知客户端-->"+(f.length() + ","+f.getName()+"\r\n"));
												//bw.newLine();// 写一行数据出去
											
												//循环向客户端发送文件内容
												
												
												InputStream socketIN= socket.getInputStream() ;
												// 获取到了文件的大小
												int ans =socketIN.read();
												System.out.println("服务器收到客户端发来的-->"+ans);
												
												InputStream is =new FileInputStream(f);
												byte datas[]=new byte[1024*5];
												int len ;
												int tlen=0;
												while((len=is.read(datas)) !=-1 ) {
													os.write(datas,0,len);
													tlen+=len;
													System.out.println("正在传输-->"+tlen);
													jpb.setValue(tlen);
												}
												
												is.close();
												

												System.out.println("已经成功向-->" + sip + "发送文件");
											} catch (IOException e1) {
												e1.printStackTrace();
											}
											
											//listprocess 中是不是有 processbar
											//pplist.remove(index);
											//将processbar也删掉
											
											
											index++;//判断下一个ip了
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
			// 中间区域面板，带滚动条
			JScrollPane scrollPane = new JScrollPane();
			getContentPane().add(scrollPane, BorderLayout.CENTER);

			scrollPane.setViewportView(panel);

		}
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLableTime();
	}

	/**
	 * 设置时间
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
