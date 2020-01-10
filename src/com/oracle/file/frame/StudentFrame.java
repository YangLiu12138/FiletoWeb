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
 * �ļ����ն�
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
			JButton btn = new JButton("���Ӳ�������");
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new ClientSetFrame().setVisible(true);

				}
			});
			toolBar.add(btn);
		}
		{
			btnStart = new JButton("���ӷ�����");
			btnStart.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// �����߳����ӷ�����
					new Thread(new Runnable() {

						@Override
						public void run() {

							try {
								Socket s = new Socket(ClientSet.ip, ClientSet.port);
								JOptionPane.showMessageDialog(null, "�ѳɹ��������˷�����");
								btnStart.setVisible(false);
								btnStop.setVisible(true);
								// �����ȴ������������ļ�����
								InputStream is = s.getInputStream();
								 while (true) {
									 System.out.println("������������--->>>>>>>>>>>>>>");
									try {

										byte tzdatas[]=new byte[400];
										//�̶���ȡ400�ֽ�
										for (int i = 0; i < 400; i++) {
											tzdatas[i]=(byte)is.read();
										}
										
										
										// ��ȡ�����ļ��Ĵ�С
										String msg = new String(tzdatas);
										msg=msg.trim();//ȥ���ո�
										System.out.println("�ͻ��˽��յ���-->"+msg);
										long llen = Long.parseLong(msg.split(",")[0]);
										String fname = msg.split(",")[1];
										
										
										OutputStream os = s.getOutputStream()  ;
										os.write( "1\r\n".getBytes());
										//bw.newLine();// дһ�����ݳ�ȥ

										// ��ѡ����ļ�
										// JOptionPane.showMessageDialog(null, file.getAbsolutePath());

										File destFile = new File(ClientSet.path, fname);
										OutputStream fos = new FileOutputStream(destFile);
										// ����Ҫ�ֶ����ļ���
										// FileUtils.copyFile(file, destFile);

										byte datas[] = new byte[1024 * 5];
										int len;// ÿ�ζ�ȡ�ĳ���
										int readLen = 0;// �ۼƶ�ȡ�ĳ���
										while (true) {
											len = is.read(datas);
											fos.write(datas, 0, len);
											readLen += len;
											if (readLen == llen) {
												break;// ��ȡ���
											}
										}

										fos.close();

										JOptionPane.showMessageDialog(null, "�洢���");
										// ��ӵ������
										dataModel.addRow(new Object[] { destFile.getName(), destFile.length(),
												destFile.getAbsolutePath(), DateUtil.getDate(DateUtil.DATE1) });

									} catch (Exception e) {
										JOptionPane.showMessageDialog(null, "������-->" + e.getMessage());
									}

								 }

							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, "������ " + e.getMessage());
							}

						}
					}).start();

				}
			});
			toolBar.add(btnStart);
		}
		{
			btnStop = new JButton("�Ͽ�����");
			toolBar.add(btnStop);
			btnStop.setVisible(false);
		}

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		scrollPane.setViewportView(table);

		dataModel.addColumn("�ļ���");
		dataModel.addColumn("�ļ���С");
		dataModel.addColumn("·��");
		dataModel.addColumn("����ʱ��");

		table.setModel(dataModel);

		JProgressBar progressBar = new JProgressBar();
		getContentPane().add(progressBar, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

	}
}
