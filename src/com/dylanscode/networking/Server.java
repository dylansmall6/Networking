package com.dylanscode.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket server;
	public Server() {
		try {
			server = new ServerSocket(33433);
			Socket socket = server.accept();
			while(true) {
				DataInputStream in = new DataInputStream(socket.getInputStream());
				System.out.println("Socket says: " + in.readUTF());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("HELLO SAYS SERVER");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
