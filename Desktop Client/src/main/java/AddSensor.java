// importing dependencies
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Button;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@SuppressWarnings("serial")
// class definition
public class AddSensor extends JFrame implements ActionListener{

	// declaring variables
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;	
	public String SensorName;
	public String FloorNumber;
	public String RoomNumber;
	

	/**
	 * Launch the application.
	 */
	
	// main function
	public static void main(String[] args) {		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// create new AddSensor object
					AddSensor frame = new AddSensor();
					
					// start visible
					frame.setVisible(true);
				} catch (Exception e) {
					// print error
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	// Add sensor function
	public AddSensor() {
		// set values
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 729, 476);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Button button = new Button("Register New Alarm");
		button.addActionListener(this);
		button.setFont(new Font("Dialog", Font.BOLD, 16));
		button.setForeground(Color.BLACK);
		button.setBackground(Color.LIGHT_GRAY);
		button.setBounds(246, 342, 211, 48);
		contentPane.add(button);
		
		
		textField = new JTextField();
		textField.setBackground(new Color(250, 250, 210));
		textField.setBounds(319, 176, 283, 36);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblFloorNo = new JLabel("Floor NO");
		lblFloorNo.setForeground(Color.BLACK);
		lblFloorNo.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblFloorNo.setBounds(169, 174, 114, 36);
		contentPane.add(lblFloorNo);
		
		JLabel lblRoomNo = new JLabel("Room No");
		lblRoomNo.setForeground(Color.BLACK);
		lblRoomNo.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblRoomNo.setBounds(172, 247, 111, 21);
		contentPane.add(lblRoomNo);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(319, 241, 283, 36);
		contentPane.add(textField_1);
		
		JLabel lblNewLabel_1 = new JLabel("Add New Alarm");
		lblNewLabel_1.setForeground(Color.BLACK);
		lblNewLabel_1.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 19));
		lblNewLabel_1.setBounds(275, 13, 171, 57);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblSensorName = new JLabel("Sensor Name");
		lblSensorName.setForeground(Color.BLACK);
		lblSensorName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblSensorName.setBounds(169, 109, 114, 36);
		contentPane.add(lblSensorName);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBackground(new Color(250, 250, 210));
		textField_2.setBounds(319, 111, 283, 36);
		contentPane.add(textField_2);
	}


	// when administrator check register new sensor button, then this function will execute
	@Override
	public void actionPerformed(ActionEvent arg0) {

		// try catch block
		try {
			// create a register and looking for running RMI server
			Registry reg = LocateRegistry.getRegistry("localhost", 1099);
			
			// create a sensor interface object
			final SensorInterface server = (SensorInterface)reg.lookup("rmi://localhost/service");
			
			// fetching values from the inputs
			SensorName = textField_2.getText();
			FloorNumber = textField.getText();
			RoomNumber = textField_1.getText();
			
			// send request to sever with values
			String response = server.createSensor(SensorName, Integer.parseInt(FloorNumber), Integer.parseInt(RoomNumber));

			// condition
			if(response.equals("true")) {
				// print success message
				System.out.println("created successfully!");
				JOptionPane.showMessageDialog(null, "Created successfully.");
				
				// after message frame will disappear
				dispose();
			}else {
				// print error message
				JOptionPane.showMessageDialog(null, "Room number already exist!!");
			}
						
		} catch (RemoteException e) {
			// print error
			e.printStackTrace();
		} catch (NotBoundException e) {
			// print errot
			e.printStackTrace();
		}
	}
}

