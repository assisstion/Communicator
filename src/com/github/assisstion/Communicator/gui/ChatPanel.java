package com.github.assisstion.Communicator.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.assisstion.Communicator.message.MessageCommandProcessor;
import com.github.assisstion.Communicator.message.MessageProcessor;

public class ChatPanel extends JPanel implements Runnable{

	private static final long serialVersionUID = -3723376082239462955L;

	protected int id;

	protected Logger logger;
	protected LoggerPane loggerPane;
	private JPanel panel;
	private JTextField textField;
	private JButton btnGo;

	protected PipedOutputStream pos;
	protected PipedInputStream pis;
	protected DataOutputStream dos;
	protected DataInputStream dis;

	protected MessageProcessor mp;

	private boolean done;
	private JPanel panel_1;
	private JLabel lblNick;
	private JTextField nick;

	public ChatPanel(MessageProcessor processor){

		Random random = new Random();
		id = random.nextInt(100000);

		mp = processor;

		logger = Logger.getLogger("chat_" + id);
		setLayout(new BorderLayout(0, 0));
		loggerPane = new LoggerPane(logger, false);
		add(loggerPane);

		processor.setLogger(logger);

		panel = new JPanel();
		loggerPane.add(panel, BorderLayout.SOUTH);

		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(25);

		btnGo = new JButton("Go");
		btnGo.addActionListener(event -> {
			try{
				dos.writeUTF(textField.getText());
				dos.flush();
				textField.setText("");
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		panel.add(btnGo);

		panel_1 = new JPanel();
		loggerPane.add(panel_1, BorderLayout.NORTH);

		lblNick = new JLabel("Name:");
		panel_1.add(lblNick);

		nick = new JTextField();
		panel_1.add(nick);
		nick.setColumns(10);

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
	}

	public void terminate(){
		try{
			dis.close();
			dos.close();
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		done = false;
		try{
			while(true){
				String input = null;
				try{
					input = dis.readUTF();
				}
				catch(IOException e){
					break;
				}
				MessageCommandProcessor mcp = mp.getCommandProcessor();
				if(mcp == null || !mcp.isCommand(mcp.processOut(input))){
					String nickInput = nick.getText();
					if(nickInput.length() == 0){
						nickInput = "Guest_" + id;
					}
					input = nickInput + ": " + input;
					if(logger.isLoggable(Level.INFO)){
						if(mcp != null){
							logger.info(mcp.processOut(input));
						}
						else{
							logger.info(input);
						}
					}
				}
				try{
					mp.output(input, false);
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		finally{
			done = true;
		}
	}

	public boolean isDone(){
		return done;
	}
}
