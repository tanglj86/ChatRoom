package com.cwddd.chat;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends Frame {

	private TextField textField;
	private TextArea textArea;
	private Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;

	public static void main(String[] args) {
		new ChatClient().LauchFrame();

	}

	private void LauchFrame() {
		textField = new TextField();
		textArea = new TextArea();
		setLocation(200, 300);
		// setSize(500, 500);
		setTitle("ChatRoom");
		add(textField, BorderLayout.SOUTH);
		add(textArea, BorderLayout.NORTH);
		pack();
		setVisible(true);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				try {
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		textField.addActionListener(new MyTFActionListener());
		connect();
	}

	private void connect() {
		try {
			s = new Socket("127.0.0.1", 8888);
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class MyTFActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = textField.getText();
			// textArea.setText(text);
			try {
				dos.writeUTF(text);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			textField.setText("");
		}

	}

}
