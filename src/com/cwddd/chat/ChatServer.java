package com.cwddd.chat;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

	public static void main(String[] args) {
		new ChatServer().start();
	}

	private void start() {
		DataInputStream dis = null;
		ServerSocket ss = null;
		boolean bStarted = false;
		try {
			ss = new ServerSocket(8888);
		} catch (BindException e) {
			System.out.println("端口使用中,请关掉重新运行程序");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bStarted = true;
			while (bStarted) {
				Socket socket = ss.accept();
				Client client = new Client(socket);
				System.out.println("a client connected!");
				new Thread(client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bStarted = false;
				if (ss != null)
					ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	class Client implements Runnable {
		private Socket s;
		private DataInputStream dis;
		private boolean bConnected = false;

		public Client(Socket s) {
			this.s = s;
			bConnected = true;
			try {
				this.dis = new DataInputStream(s.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (bConnected) {
				try {
					String string = dis.readUTF();
					System.out.println(string);
				} catch (EOFException e) {
					System.out.println("Client closed");
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					bConnected = false;
					try {
						if (dis != null)
							dis.close();
						if (s != null)
							s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

}
