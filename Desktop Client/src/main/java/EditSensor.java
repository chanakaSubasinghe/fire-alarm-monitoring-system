// importing dependencies 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Button;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@SuppressWarnings("serial")
// class definition
public class EditSensor extends JFrame implements ActionListener {

	// declaring variables
	private JPanel contentPane;
	public static JTextField textField;
	public static JTextField textField_1;
	public static JTextField textField_2;
	
	public String SensorName;
	public String FloorNumber;
	public String RoomNumber;
	
	public static String _id;
	
	JButton btnDelete = new JButton("Delete");

	// edit sensor function
	public EditSensor(String id,String sensorName, String floorNumber, String roomNumber) {
		// set values
		setBackground(Color.GRAY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 729, 476);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Button button = new Button("Edit & Exit");
		button.addActionListener(this);
		button.setFont(new Font("Dialog", Font.BOLD, 16));
		button.setForeground(Color.BLACK);
		button.setBackground(Color.LIGHT_GRAY);
		button.setBounds(246, 342, 211, 48);
		contentPane.add(button);
	
		
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
		
		JLabel lblNewLabel_1 = new JLabel("Edit a Alarm");
		lblNewLabel_1.setForeground(Color.BLACK);
		lblNewLabel_1.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 19));
		lblNewLabel_1.setBounds(308, 13, 114, 57);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblSensorName = new JLabel("Sensor Name");
		lblSensorName.setForeground(Color.BLACK);
		lblSensorName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblSensorName.setBounds(169, 109, 114, 36);
		contentPane.add(lblSensorName);
		
		textField = new JTextField();	
		textField.setBackground(new Color(250, 250, 210));
		textField.setBounds(319, 176, 283, 36);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(319, 241, 283, 36);
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBackground(new Color(250, 250, 210));
		textField_2.setBounds(319, 111, 283, 36);
		contentPane.add(textField_2);
		
		_id = id;
		textField.setText(floorNumber);
		textField_1.setText(roomNumber);
		textField_2.setText(sensorName);
		
		btnDelete.setBounds(54, 65, 105, 36);
		btnDelete.addActionListener(this);
		contentPane.add(btnDelete);
		
	}

	// action performed function
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// try catch block
		try {
			// create source object to fetch current button details
			Object source = arg0.getSource();
			
			// create a register and looking for running RMI server
			Registry reg = LocateRegistry.getRegistry("localhost", 1099);
			
			// condition			
	        if(source == btnDelete) {
	        	
	        	// create a sensor interface object
	        	final SensorInterface server = (SensorInterface)reg.lookup("rmi://localhost/service");
	        	
	        	// send request to server and get response
	        	String response = server.deleteSensor(_id);
	        	
	        	// condition
    			if(response.equals("true")) {
    				// success message
    				System.out.println("deleted successfully!");
    			}else {
    				// error message
    				System.out.println("Something went wrong!");
    			}

            }else {
            	// create a sensor interface object
            	final SensorInterface server = (SensorInterface)reg.lookup("rmi://localhost/service");
    			
            	// getting values
    			SensorName = textField_2.getText();
    			FloorNumber = textField.getText();
    			RoomNumber = textField_1.getText();
    			
    			// convert strings to integer
    			int floorNumber = Integer.parseInt(FloorNumber);
    			int roomNumber = Integer.parseInt(RoomNumber);

    			// send request to server and get response
    			String response = server.updateSensor(_id, SensorName, floorNumber, roomNumber);
    			
    			// condition
    			if(response.equals("true")) {
    				// success message
    				System.out.println("updated successfully!");
    			}else {
    				// error message
    				System.out.println("Something went wrong!");
    			}		    		
            }

			// end of this function frame will disappear
			dispose();
		} catch (RemoteException e) {
			// print error
			e.printStackTrace();
		} catch (NotBoundException e) {
			// print error
			e.printStackTrace();
		}		
	}
}