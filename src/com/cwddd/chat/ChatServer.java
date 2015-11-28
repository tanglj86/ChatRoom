package com.cwddd.chat;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

	public static void main(String[] args) {
		DataInputStream dis = null;
		ServerSocket ss = null;
		Socket socket = null;
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
				boolean bConnected = false;
				socket = ss.accept();
				bConnected = true;
				System.out.println("a client connected!");
				dis = new DataInputStream(socket.getInputStream());
				while (bConnected) {
					try {
						String string = dis.readUTF();
						System.out.println(string);
					} catch (EOFException e) {
						System.out.println("Client closed");
					} catch (Exception e) {
						if (dis != null)
							dis.close();
						if (socket != null)
							socket.close();
						bConnected = false;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (dis != null)
					dis.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
