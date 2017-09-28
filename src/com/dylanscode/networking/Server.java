package com.dylanscode.networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
	private ServerSocket server;
	protected Vector<ClientHandler> handlers = new Vector<ClientHandler>();
	public Server() {
		try {
			server = new ServerSocket(33433);
			while(true) {
				Socket socket = server.accept();
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				ClientHandler handler = new ClientHandler(socket, out, in);
				handlers.add(handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
	class ClientHandler implements Runnable{
		private Socket socket;
		private DataOutputStream out;
		private DataInputStream in;
		public ClientHandler(Socket socket, DataOutputStream out, DataInputStream in) {
			this.socket = socket;
			this.out = out;
			this.in = in;
		}
		@Override
		public void run() {
			for(ClientHandler handler : handlers) {
				synchronized(handler) {
					
				}
			}
		}
		public synchronized void start() {
			new Thread(this).start();
		}
		
	}
}
