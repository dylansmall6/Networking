package com.dylanscode.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
/**
 * The ClientHandler class is going to serve the purpose of handling sending data to each client
 * We need this class to implement Runnable because we are going to have multiple iterations of this class during the program.
 * @author Dylan
 */
public class ClientHandler implements Runnable {
	//We need a socket, and its DataInputStream/DataOutputStream in order to send data
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	/**
	 * Basic Constructor that assigns instance variables
	 * @param socket
	 * @param in
	 * @param out
	 */
	public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out) {
		this.socket = socket;
		this.in = in;
		this.out = out;
	}
	@Override
	public void run() {
		try {
			while(true) {
				//While the thread is running, we want to first read a recieved String
				String recievedString = in.readUTF();
				//Then loop through the Vector of ClientHandlers
				synchronized (Server.handlers){
					for(ClientHandler handler : Server.handlers) {
						//And for each ClientHandler, we want to write out the message to its DataOutputStream
						synchronized(handler.out) {
							handler.out.writeUTF(recievedString);
						}
						//Then flush() to make sure message goes through
						out.flush();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				//After the thread is closed, we call close()
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Closes the Socket and its I/O
	 * @throws IOException
	 */
	public synchronized void close() throws IOException {
		in.close();
		out.close();
		socket.close();
		Server.handlers.remove(this);
	}
	/**
	 * Calls the run() method
	 */
	public synchronized void start() {
		new Thread(this).start();
	}
}
