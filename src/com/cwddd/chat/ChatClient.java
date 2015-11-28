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
	private boolean bConnected;
	
	Thread thread = new Thread(new ReceivThread());
	
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
				disconnect();
			}
		});
		textField.addActionListener(new MyTFActionListener());
		connect();
		thread.start();
		
	}

	private void disconnect() {
		try {
			bConnected = false;
			dis.close();
			dos.close();
			s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void connect() {
		try {
			s = new Socket("127.0.0.1", 8888);
			bConnected = true;
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
				dos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			textField.setText("");
		}

	}

	private class ReceivThread implements Runnable {

		@Override
		public void run() {
			while (bConnected) {
				try {
					String string = dis.readUTF();
					textArea.setText(textArea.getText() + string + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
