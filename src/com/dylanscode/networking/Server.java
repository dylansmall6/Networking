package com.dylanscode.networking;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server{
	//We use vectors because they are "thread safe" meaning they are synchronized. Synchronized is a key word which states that only one thread at a time
	//may be able to write or recieve data. This prevents corrupted data, which would happen often if there was no synchronization, because java swing is NOT
	//thread friendly
	public static Vector<ChatHandler> chatHandlers = new Vector<ChatHandler>();
	ServerSocket mainServer;
	public Server() {
		try {
			mainServer = new ServerSocket(33233);
			while(true) {
				//looks for new socket to connect
				Socket socket = mainServer.accept();
				//After socket is found, it instantiates a new ChatHandler and adds it to Vector
				ChatHandler handler = new ChatHandler(socket);
				chatHandlers.add(handler);
				handler.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
