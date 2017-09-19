package com.dylanscode.networking;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements Runnable, ActionListener {
	private DataInputStream in;
	private DataOutputStream out;
	// The listener thread reads data being sent from the server to the client
	private Thread listenThread;
	private JTextField textField;
	private JTextArea textArea;
	private static final long serialVersionUID = 1L;
	boolean isConnected = false;
	BufferStrategy bs;
	Graphics g;

	public Client(DataInputStream in, DataOutputStream out) {
		setTitle("Text Messenger");
		this.in = in;
		this.out = out;
		Dimension size = new Dimension(400, 400);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		setVisible(true);
		setResizable(false);
		setLayout(new GridBagLayout());
		textArea = new JTextArea(21, 20);
		textArea.setEditable(false);
		JScrollPane sPane = new JScrollPane(textArea);
		textField = new JTextField(20);
		textField.addActionListener(this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(sPane, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		add(textField, c);
		pack();
		textField.requestFocus();
		listenThread = new Thread(this);
		listenThread.start();
		setLocationRelativeTo(null);

	}

	public static void main(String[] args) {
		try {
			// starts a connection on the LAN and on port 33233
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost", 33233);
			new Client(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				String recievedString = in.readUTF();
				textArea.append(recievedString + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			out.writeUTF(textField.getText());
			// flush makes sure the text is sent immediately
			// out.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		textField.setText("");
	}
}