package com.dylanscode.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
/**
 * The Server class is going to serve the purpose of bringing in sockets and assigning them to a ClientHandler
 * @author Dylan
 */
public class Server {
	//The Server is an essential part. All Sockets connect to the ServerSocket, so 
	//we need a ServerSocket to connec to
	private ServerSocket server;
	//Here we have a global Vector of ClientHandlers. We are using vectors here because they
	//are thread safe, so no concurrancy issues are expected!
	public static Vector<ClientHandler> handlers = new Vector<ClientHandler>();
	public Server() {
		try {
			//initializes a ServerSocket for Sockets to connect to on this port on the IP of the computer running the Server
			server = new ServerSocket(33433);
			while(true) {
				//While the Server is running, we first want to look for a Socket to connect to
				Socket socket = server.accept();
				//Then, we initialize its Input and Outputs
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				//Finally, we create a ClientHandler to handle all messages from Client.
				//We then add the handler to the Vector, and start the ClientHandler since it is a thread
				//on its own
				ClientHandler handler = new ClientHandler(socket,in,out);
				handlers.add(handler);
				handler.start();
			}
		}catch(IOException e) {
			System.err.println("IO Exception");
		}
	}
	/**
	 * Creates a new instance of the Server class
	 * @param args
	 */
	public static void main(String[] args) {
		new Server();
	}
}
