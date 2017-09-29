package com.dylanscode.networking;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The Client class is going to serve the purpose of making a JFrame for input, and creating a Socket to write to a Server
 * @author Dylan
 */
public class Client extends JFrame implements ActionListener, Runnable {

	private static final long serialVersionUID = 1L;
	private boolean running = false;
	// Sockets are a networking tool used for connecting to ServerSockets. When a
	// socket is initialized, It attempts to connect
	// to a ServerSocket with a specific INetAddress (IP) and Port. For this program
	// we use the localhost and port 33433.
	private Socket socket;
	// Each Socket has its own stream of data that comes into the socket, and comes
	// out of it. This way
	// we can write data to other the ServerSocket
	private DataOutputStream out;
	private DataInputStream in;
	// JTextField is input (Where messages are typed) on the JFrame
	private JTextField input;
	// JTextArea is just a box of text that we can append other messages with. This
	// also goes on the JFrame
	private JTextArea area;
	// Username of the Client. We initialize this with a JOptionPane later
	private String uname = "";

	/**
	 * This main method creates a new Client object and calls the start() method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Client().start();
	}

	/**
	 * The init() method makes sure everything is set up right before we start
	 * chatting
	 */
	public void init() {
		//We want to make sure the user inputs a name
		while(uname.equals("")) {
			//This JOptionPane is casted to a String, because showInputDialog returns an Object. This statement
			//Just makes a basic JOptionPane with a question asking for a username
			uname = (String) JOptionPane.showInputDialog(this.getContentPane(), "Welcome to the chat room.\nUsername?",
					"Chat Client", JOptionPane.PLAIN_MESSAGE, null, null, null);
		}
		//Setting the size of the Client JFrame
		setMinimumSize(new Dimension(300, 400));
		setMaximumSize(new Dimension(300, 400));
		setPreferredSize(new Dimension(300, 400));
		setSize(new Dimension(300, 400));
		//Misc JFrame stuff
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//We are using GridBagLayout because it is the most versatile of the layouts provided by java
		setLayout(new GridBagLayout());
		//Initializing the JTextField & JTextArea
		input = new JTextField(24);
		input.addActionListener(this);
		area = new JTextArea(21, 24);
		area.setEditable(false);
		//We are adding a JScrollPane to the JTextArea so we can scroll to see messages!
		JScrollPane scrollPane = new JScrollPane(area);
		GridBagConstraints c = new GridBagConstraints();
		add(scrollPane, c);
		c.gridy = 1;
		add(input, c);
		//Finalizing JFrame
		setTitle("Messenger: " + uname);
		setVisible(true);
		pack();
		//Here we are going to try to connect to the ServerSocket
		try {
			//We initialize the socket to connect to localhost on port 33433
			socket = new Socket("localhost", 33433);
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * start() creates a thread, calling the run() method
	 */
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		//Initialize first
		init();
		while (running) {
			//While the thread is running we just want to check for Strings
			//to append to our JTextArea
			try {
				String str = in.readUTF();
				area.append(str + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			//actionPerformed() is called when one hits enter on the keyboard after typing in the JTextField
			//Here, we write the messages on the DataOutputStream, then flush() to make sure it goes through
			out.writeUTF(uname + ": " + input.getText());
			out.flush();
			//Reset the JTextField to a blank state
			input.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
