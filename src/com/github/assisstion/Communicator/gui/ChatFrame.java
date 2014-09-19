package com.github.assisstion.Communicator.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.github.assisstion.Communicator.relay.ASocketClient;
import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.ASocketServer;
import com.github.assisstion.Communicator.relay.CSocketHelper;
import com.github.assisstion.Communicator.relay.message.MessageProcessor;

public class ChatFrame extends JFrame{

	/**
	 *
	 */
	private static final long serialVersionUID = -3551670774266923639L;
	private JPanel contentPane;
	private JTextField cHost;
	private JTextField cPort;
	private JTextField sPort;
	protected ChatPanel panel;
	private JPanel panelX;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				try{
					ChatFrame frame = new ChatFrame();
					frame.setVisible(true);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panelX = new JPanel();
		contentPane.add(panelX, BorderLayout.CENTER);
		panelX.setLayout(new BoxLayout(panelX, BoxLayout.Y_AXIS));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Client", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelX.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);

		JLabel lblLocation = new JLabel("Host");
		panel_4.add(lblLocation);

		cHost = new JTextField();
		panel_4.add(cHost);
		cHost.setColumns(10);

		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);

		JLabel lblPort = new JLabel("Port");
		panel_7.add(lblPort);

		cPort = new JTextField();
		panel_7.add(cPort);
		cPort.setColumns(10);

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);

		JButton btnStartClient = new JButton("Start Client");
		btnStartClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessageProcessor processor = new MessageProcessor();
				try{
					startClient(cHost.getText(), Integer.parseInt(cPort.getText()), processor);
				}
				catch(NumberFormatException | IOException e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_3.add(btnStartClient);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Server", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelX.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5);

		JLabel lblPort_1 = new JLabel("Port");
		panel_5.add(lblPort_1);

		sPort = new JTextField();
		panel_5.add(sPort);
		sPort.setColumns(10);

		JPanel panel_6 = new JPanel();
		panel_2.add(panel_6);

		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MessageProcessor processor = new MessageProcessor();
				try{
					startServer(Integer.parseInt(sPort.getText()), processor);
				}
				catch(NumberFormatException | IOException e1){
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_6.add(btnStartServer);
	}

	public void startClient(String host, int port, MessageProcessor process) throws IOException{
		panel = new ChatPanel(process);
		contentPane.remove(panelX);
		contentPane.add(panel);
		new Thread(new ClientStarter(host, port, process)).start();
		validate();
	}

	public class ClientStarter implements Runnable{

		protected String host;
		protected int port;
		protected MessageProcessor process;

		public ClientStarter(String host, int port, MessageProcessor process){
			this.host = host;
			this.port = port;
			this.process = process;
		}

		@Override
		public void run(){
			try(
					ASocketClient<ASocketHandler> client =
					CSocketHelper.getClient(host, port, process);
					BufferedReader in = new BufferedReader(new InputStreamReader(System.in))){
				System.out.println("Started...");
				client.open();
				System.out.println("Client opened!");

				Thread chatThread = new Thread(panel);
				chatThread.start();
				synchronized(this){
					try{
						if(!panel.isDone()){
							chatThread.join();
						}
					}
					catch(InterruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Done!");
			}
			catch(IOException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void startServer(int port, MessageProcessor process) throws IOException{
		contentPane.remove(panelX);
		panel = new ChatPanel(process);
		contentPane.add(panel);
		new Thread(new ServerStarter(port, process)).start();
		validate();
	}

	public class ServerStarter implements Runnable{

		protected int port;
		protected MessageProcessor process;

		public ServerStarter(int port, MessageProcessor process){
			this.port = port;
			this.process = process;
		}

		@Override
		public void run(){
			try(ASocketServer<ASocketHandler> server =
					CSocketHelper.getServer(port, process);
					BufferedReader in = new BufferedReader(new InputStreamReader(System.in))){
				System.out.println("Started...");
				server.open();
				System.out.println("Server opened!");
				Thread chatThread = new Thread(panel);
				chatThread.start();
				synchronized(this){
					try{
						if(!panel.isDone()){
							chatThread.join();
						}
					}
					catch(InterruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Done!");
			}
			catch(IOException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
