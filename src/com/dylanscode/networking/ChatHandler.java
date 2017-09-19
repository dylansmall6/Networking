package com.dylanscode.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;

//extends thread because we call start method from Server.java
public class ChatHandler extends Thread{
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	public ChatHandler(Socket client) {
		this.socket = client;
		try {
			this.in = new DataInputStream(client.getInputStream());
			this.out = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			Server.chatHandlers.add(this);
			while(true) {
				String serverMessage = in.readUTF();
				sendToAll(serverMessage);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			Server.chatHandlers.remove(this);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void sendToAll(String string) {
		synchronized(Server.chatHandlers) {
			Enumeration e = Server.chatHandlers.elements();
			while(e.hasMoreElements()) {
				ChatHandler handler = (ChatHandler) e.nextElement();
				try {
					synchronized(handler.out) {
						handler.out.writeUTF(string);
					}
					handler.out.flush();
				}catch(Exception ex) {
					ex.printStackTrace();
				}

			}
		}
	}
}
