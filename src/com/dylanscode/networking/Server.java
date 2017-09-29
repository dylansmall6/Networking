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
<<<<<<< HEAD
	public static Vector<ClientHandler> handlers = new Vector<ClientHandler>();
=======
	protected Vector<ClientHandler> handlers = new Vector<ClientHandler>();
>>>>>>> b5922924ddd872d1606216eae18886e41e6bc4a9
	public Server() {
		try {
			server = new ServerSocket(33433);
			while(true) {
				Socket socket = server.accept();
<<<<<<< HEAD
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				ClientHandler handler = new ClientHandler(socket,in,out);
				handlers.add(handler);
				handler.start();
=======
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				ClientHandler handler = new ClientHandler(socket, out, in);
				handlers.add(handler);
>>>>>>> b5922924ddd872d1606216eae18886e41e6bc4a9
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
