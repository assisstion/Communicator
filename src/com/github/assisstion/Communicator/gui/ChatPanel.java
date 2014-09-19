package com.github.assisstion.Communicator.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatPanel extends JPanel implements Runnable{

	private static final long serialVersionUID = -3723376082239462955L;

	protected Logger logger;
	protected LoggerPane loggerPane;
	protected String location;
	private JPanel panel;
	private JTextField textField;
	private JButton btnGo;

	protected PipedOutputStream pos;
	protected PipedInputStream pis;
	protected DataOutputStream dos;
	protected DataInputStream dis;

	protected Scanner sc;
	public ChatPanel(String location, boolean excludeOn){
		this.location = location;

		logger = Logger.getLogger("main");
		setLayout(new BorderLayout(0, 0));
		loggerPane = new LoggerPane(logger, false);
		add(loggerPane);

		panel = new JPanel();
		loggerPane.add(panel, BorderLayout.SOUTH);

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(25);

		btnGo = new JButton("Go");
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try{
					dos.writeUTF(textField.getText());
					dos.flush();
					textField.setText("");
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		panel.add(btnGo);

		textField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					btnGo.doClick();
				}
			}
		});

		pos = new PipedOutputStream();
		try{
			pis = new PipedInputStream(pos);
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dos = new DataOutputStream(pos);
		dis = new DataInputStream(pis);

		sc = new Scanner(dis);
	}

	@Override
	public void run(){
		//TODO incomplete
	}
}
