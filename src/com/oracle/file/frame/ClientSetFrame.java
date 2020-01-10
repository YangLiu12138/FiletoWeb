package com.oracle.file.frame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.oracle.file.entity.ClientSet;
import com.oracle.file.entity.ServerMsg;
import com.sun.security.ntlm.Client;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientSetFrame extends JFrame {
	private JTextField txtPort;
	private JTextField txtIP;
	private JTextField txtPath;

	public ClientSetFrame() {
		setResizable(false);
		setTitle("\u5BA2\u6237\u7AEF\u8BBE\u7F6E");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(289, 284);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		lblNewLabel.setBounds(29, 92, 77, 25);
		getContentPane().add(lblNewLabel);

		txtPort = new JTextField();
		txtPort.setText("8888");
		txtPort.setBounds(102, 92, 138, 30);
		getContentPane().add(txtPort);
		txtPort.setColumns(10);

		JButton btnNewButton = new JButton("设置");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String port = txtPort.getText();
				String ip = txtIP.getText();
				String path = txtPath.getText();
				ClientSet.ip = ip;
				ClientSet.port = Integer.parseInt(port);
				ClientSet.path = path;
				JOptionPane.showMessageDialog(null, "设置成功");
				ClientSetFrame.this.dispose();

			}
		});
		btnNewButton.setBounds(80, 199, 161, 30);
		getContentPane().add(btnNewButton);

		JLabel lblIp = new JLabel("IP\u5730\u5740\uFF1A");
		lblIp.setFont(new Font("宋体", Font.PLAIN, 18));
		lblIp.setBounds(29, 33, 77, 25);
		getContentPane().add(lblIp);

		txtIP = new JTextField();
		txtIP.setText("192.168.0.103");
		txtIP.setColumns(10);
		txtIP.setBounds(102, 33, 138, 30);
		getContentPane().add(txtIP);

		txtPath = new JTextField();
		txtPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int count = e.getClickCount();
				if (count == 2) {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					int result = jfc.showSaveDialog(null);
					if (result == 0) {
						String path = jfc.getSelectedFile().getAbsolutePath();
						txtPath.setText(path);
					}
				}
			}
		});
		txtPath.setText("d:\\revice_java");
		txtPath.setBounds(102, 146, 138, 31);
		getContentPane().add(txtPath);
		txtPath.setColumns(10);

		JLabel label = new JLabel("\u5B58\u653E\u5730\u5740\uFF1A");
		label.setFont(new Font("宋体", Font.PLAIN, 18));
		label.setBounds(10, 146, 96, 25);
		getContentPane().add(label);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

}
