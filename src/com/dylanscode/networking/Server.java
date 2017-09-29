package com.dylanscode.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	private ServerSocket server;
	public static Vector<ClientHandler> handlers = new Vector<ClientHandler>();
	public Server() {
		try {
			server = new ServerSocket(33433);
			while(true) {
				Socket socket = server.accept();
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				ClientHandler handler = new ClientHandler(socket,in,out);
				handlers.add(handler);
				handler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
