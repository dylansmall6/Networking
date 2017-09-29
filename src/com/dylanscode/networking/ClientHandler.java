package com.dylanscode.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}
	@Override
	public void run() {
		try {
			while(true) {
				String recievedString = in.readUTF();
				synchronized (Server.handlers){
					for(ClientHandler handler : Server.handlers) {
						synchronized(handler.out) {
							handler.out.writeUTF(recievedString);
						}
						out.flush();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized void close() throws IOException {
		in.close();
		out.close();
		socket.close();
		Server.handlers.remove(this);
	}
	public synchronized void start() {
		new Thread(this).start();
	}
}
