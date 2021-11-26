package client;

import java.awt.EventQueue;
import java.awt.event.*;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import manager.ManagerInterface;

import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.JTextPane;

public class MapRedFrame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField Text_Area;
	private JTextPane textPane;
	private ManagerInterface skeleton;

	
	

	/**
	 * Create the frame.
	 */
	public MapRedFrame(ManagerInterface sk) {
		this.setSkeleton(sk);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 707, 564);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.textHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_Result = new JPanel();
		panel_Result.setBounds(10, 363, 669, 153);
		contentPane.add(panel_Result);
		panel_Result.setLayout(null);
		
		textPane = new JTextPane();
		textPane.setBounds(10, 31, 649, 111);
		panel_Result.add(textPane);
		
		JPanel panel_Text = new JPanel();
		panel_Text.setBounds(10, 75, 669, 263);
		contentPane.add(panel_Text);
		panel_Text.setLayout(null);
		
		JLabel label_Info = new JLabel("Veuillez taper votre texte ci dessous:");
		label_Info.setHorizontalAlignment(SwingConstants.CENTER);
		label_Info.setBounds(146, 11, 325, 25);
		label_Info.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_Text.add(label_Info);
		
		Text_Area = new JTextField();
		Text_Area.setHorizontalAlignment(SwingConstants.LEFT);
		Text_Area.setText("Ici, j'\u00E9cris mon texte");
		Text_Area.setBounds(10, 47, 649, 176);
		panel_Text.add(Text_Area);
		Text_Area.setColumns(10);
		
		JButton btn_Submit = new JButton("Submit");
		btn_Submit.setActionCommand("btn_Submit");
		btn_Submit.addActionListener(this);
		/*
		btn_Submit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				// Save the text in a string 
				String textToTreat = Text_Area.getText();
				
				// Call the function for the MapReducing
				
				// For test: display the text on the panel_Result
				textPane.setText(this);
				
				// Clear Text_Area
				Text_Area.setText("");
				
			}
		});*/

		btn_Submit.setBounds(291, 229, 89, 23);
		panel_Text.add(btn_Submit);
		
		JLabel LabelTitle = new JLabel("MapReducing");
		LabelTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		LabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		LabelTitle.setBounds(231, 11, 198, 38);
		contentPane.add(LabelTitle);
	}

	public void actionPerformed(ActionEvent e){
		
		try {
			textPane.setText("");
			// Save the text in a string 
			String textToTreat = Text_Area.getText();
				
			// Call the function for the MapReducing
			ArrayList<String> result;
			result = this.getSkeleton().mapReduce(textToTreat);

			// For test: display the text on the panel_Result
			textPane.setText(result.toString());
		
			// Clear Text_Area
			Text_Area.setText("New text...");
		} catch (RemoteException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		
	}

	public ManagerInterface getSkeleton(){
		return this.skeleton;
	}

	public void setSkeleton(ManagerInterface s){
		this.skeleton = s;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registry registry = LocateRegistry.getRegistry(9000);

					ManagerInterface skeleton = (ManagerInterface) registry.lookup("manager");
					MapRedFrame frame = new MapRedFrame(skeleton);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
}
