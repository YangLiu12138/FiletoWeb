package com.oracle.file.frame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.oracle.file.entity.ServerMsg;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerSetFrame extends JFrame {
	private JTextField textField;

	public ServerSetFrame() {
		setResizable(false);
		setTitle("\u670D\u52A1\u5668\u8BBE\u7F6E");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(233, 156);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("\u7AEF\u53E3\u53F7\uFF1A");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 18));
		lblNewLabel.setBounds(28, 30, 77, 25);
		getContentPane().add(lblNewLabel);

		textField = new JTextField();
		textField.setText("8888");
		textField.setBounds(101, 30, 88, 30);
		getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("设置");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String port = textField.getText();
				try {
					int p = Integer.parseInt(port);

					if (p < 0 || p > 65535) {
						JOptionPane.showMessageDialog(null, "端口号范围错误0~65535");

					} else {
						ServerMsg.port = p;
						JOptionPane.showMessageDialog(null, "端口号允许使用:"+p);
						ServerSetFrame.this.dispose();
					}

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "端口号必须为数字");
				}
			}
		});
		btnNewButton.setBounds(28, 81, 161, 30);
		getContentPane().add(btnNewButton);
	}
}
