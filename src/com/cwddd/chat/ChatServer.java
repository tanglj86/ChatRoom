package com.cwddd.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatServer {
	private List<Client> clients = new ArrayList<Client>();

	public static void main(String[] args) {
		new ChatServer().start();
	}

	private void start() {
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
				clients.add(client);
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
		private DataOutputStream dos;
		private boolean bConnected = false;

		public Client(Socket s) {
			this.s = s;
			bConnected = true;
			try {
				this.dis = new DataInputStream(s.getInputStream());
				this.dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void send(String str) {
			try {
				dos.writeUTF(str);
				dos.flush();
			} catch (IOException e) {
				clients.remove(this);
			}
		}

		@Override
		public void run() {
			Client client = null;
			try {
				while (bConnected) {
					String string = dis.readUTF();
					System.out.println(string);
					for (int i = 0; i < clients.size(); i++) {
						client = clients.get(i);
						client.send(string);
					}
				}
			} catch (EOFException e) {
				System.out.println("Client closed0");
			} catch (IOException e) {
				System.out.println("Client closed1");
			} catch (Exception e) {
				System.out.println("Client closed2");
				e.printStackTrace();
			} finally {
				bConnected = false;
				try {
					if (dis != null)
						dis.close();
					if (dos != null)
						dos.close();
					if (s != null)
						s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// if (client != null) {
				// clients.remove(client);
				// client = null;
				// }
			}
		}

	}

}
