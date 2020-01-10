package com.oracle.file.frame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;

import com.oracle.file.entity.ClientSet;
import com.oracle.util.DateUtil;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

/**
 * 文件接收端
 * 
 * @author Administrator
 *
 */
public class StudentFrame extends JFrame {
	private JTable table;
	private JButton btnStop;
	private JButton btnStart;
	DefaultTableModel dataModel = new DefaultTableModel();

	public StudentFrame() {
		setTitle("java socket thread \u6587\u4EF6\u63A5\u6536\u5BA2\u6237\u7AEF");

		setSize(460, 380);

		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);

		{
			JButton btn = new JButton("连接参数设置");
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ClientSetFrame().setVisible(true);

				}
			});
			toolBar.add(btn);
		}
		{
			btnStart = new JButton("连接服务器");
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 开启线程连接服务器
					new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								Socket s = new Socket(ClientSet.ip, ClientSet.port);
								JOptionPane.showMessageDialog(null, "已成功连接上了服务器");
								btnStart.setVisible(false);
								btnStop.setVisible(true);
								// 开启等待服务器发送文件内容
								InputStream is = s.getInputStream();
								 while (true) {
									 System.out.println("即将接受数据--->>>>>>>>>>>>>>");
									try {

										byte tzdatas[]=new byte[400];
										//固定获取400字节
										for (int i = 0; i < 400; i++) {
											tzdatas[i]=(byte)is.read();
										}
										
										
										// 获取到了文件的大小
										String msg = new String(tzdatas);
										msg=msg.trim();//去除空格
										System.out.println("客户端接收到了-->"+msg);
										long llen = Long.parseLong(msg.split(",")[0]);
										String fname = msg.split(",")[1];
										
										
										OutputStream os = s.getOutputStream()  ;
										os.write( "1\r\n".getBytes());
										//bw.newLine();// 写一行数据出去

										// 我选择的文件
										// JOptionPane.showMessageDialog(null, file.getAbsolutePath());

										File destFile = new File(ClientSet.path, fname);
										OutputStream fos = new FileOutputStream(destFile);
										// 不需要手动存文件了
										// FileUtils.copyFile(file, destFile);

										byte datas[] = new byte[1024 * 5];
										int len;// 每次读取的长度
										int readLen = 0;// 累计读取的长度
										while (true) {
											len = is.read(datas);
											fos.write(datas, 0, len);
											readLen += len;
											if (readLen == llen) {
												break;// 读取完毕
											}
										}

										fos.close();

										JOptionPane.showMessageDialog(null, "存储完毕");
										// 添加到表格中
										dataModel.addRow(new Object[] { destFile.getName(), destFile.length(),
												destFile.getAbsolutePath(), DateUtil.getDate(DateUtil.DATE1) });

									} catch (Exception e) {
										JOptionPane.showMessageDialog(null, "出错了-->" + e.getMessage());
									}

								 }

							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, "出错了 " + e.getMessage());
							}

						}
					}).start();

				}
			});
			toolBar.add(btnStart);
		}
		{
			btnStop = new JButton("断开连接");
			toolBar.add(btnStop);
			btnStop.setVisible(false);
		}

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		dataModel.addColumn("文件名");
		dataModel.addColumn("文件大小");
		dataModel.addColumn("路径");
		dataModel.addColumn("接收时间");

		table.setModel(dataModel);

		JProgressBar progressBar = new JProgressBar();
		getContentPane().add(progressBar, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

	}
}
