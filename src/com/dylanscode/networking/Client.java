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

public class Client extends JFrame implements ActionListener,Runnable {

	private static final long serialVersionUID = 1L;
	private boolean running = false;
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private JTextField input;
	private JTextArea area;
	private String uname;
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			out.writeUTF(uname + ": " + input.getText());
			out.flush();
			input.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client().start();
	}
	public synchronized void start() {
		running = true; 
		new Thread(this).start();
	}
	@Override
	public void run() {
		init();
		while(running) {
			try {
				String str = in.readUTF();
				area.append(str + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void init() {
		uname = (String)JOptionPane.showInputDialog(this.getContentPane(), "Welcome to the chat room.\nUsername?","Chat Client", JOptionPane.PLAIN_MESSAGE,null,null,null);
		setMinimumSize(new Dimension(300,400));
		setMaximumSize(new Dimension(300,400));
		setPreferredSize(new Dimension(300,400));
		setSize(new Dimension(300,400));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		input = new JTextField(24);
	    input.addActionListener(this);
	    area = new JTextArea(21,24);
	    area.setEditable(false);
	    JScrollPane scrollPane = new JScrollPane(area);
	    GridBagConstraints c = new GridBagConstraints();
	    add(scrollPane,c);
	    c.gridy = 1;
	    add(input,c);
		setTitle("Messenger: " + uname);
		setVisible(true);
		pack();
		try {
			socket = new Socket("localhost",33433);
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
