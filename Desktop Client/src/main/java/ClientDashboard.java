// importing dependencies
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.*;

import java.util.Timer;
import java.util.TimerTask;

// class definition
public class ClientDashboard extends JFrame {

	// declaring private variables
	private JPanel contentPane;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	
	// main function
	public static void main(String[] args) {
		//init function
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// try catch block
				try {
					// create clientDashboard object
					ClientDashboard frame = new ClientDashboard();
					// start to visible
					frame.setVisible(true);
				} catch (Exception e) {
					// pring error
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws NotBoundException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	// declaring client dashBoard function 
	public ClientDashboard() throws NotBoundException, JsonParseException, JsonMappingException, IOException {
		// set values
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 897, 397);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// declaring a new button - login button
		JButton btnNewButton = new JButton("Login");
		
		// listener for when user click the login button this will execute
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// create a loginFrame object
				LoginFrame ds = new LoginFrame();
				// start to visible
				ds.setVisible(true);
				dispose();
			
			}	
		});
		//set values
		btnNewButton.setFont(new Font("Arial Black", Font.BOLD, 14));
		btnNewButton.setBackground(UIManager.getColor("Button.light"));
		btnNewButton.setBounds(685, 51, 161, 41);
		contentPane.add(btnNewButton);
		
		// creating scrollPane for table
		JScrollPane scrollPane = new JScrollPane();
		// set values
		scrollPane.setBounds(89, 137, 680, 199);
		contentPane.add(scrollPane);
		
		// creating a new table object and set values
		table = new JTable();
		table.setEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setFont(new Font("Bodoni MT Black", Font.PLAIN, 17));
		
		// declare the timer
		Timer t = new Timer();
		
		// create a register and looking for running RMI server
		Registry reg = LocateRegistry.getRegistry("localhost", 1099);
		// create a sensor interface object
		final SensorInterface server = (SensorInterface)reg.lookup("rmi://localhost/service");		
		
		// getting all sensor details
		String jsonString = server.readSensors();

		// JSON response convert to JAVA object
		JSONArray arr = new JSONArray(jsonString);	
		
		// declaring headers in the table
		final Object[][] rowData = {};
		final Object[] columnNames = {"Sensor Name", "Floor Number","Room Number","CO2 level","Smoke Level", "status"};
		
		// creating table object
		final DefaultTableModel listTableModel;
		
		// parse declared row data and columns
		listTableModel = new DefaultTableModel(rowData, columnNames);
		

		//Set the schedule function and rate
		t.scheduleAtFixedRate(new TimerTask() {

		    @Override
		    public void run() {
		    	
		    	// when this loop start to run firstly, remove all row data
		    	if (listTableModel.getRowCount() > 0) {
		    		// removing every rows 
		    	    for (int i = listTableModel.getRowCount() - 1; i > -1; i--) {
		    	    	listTableModel.removeRow(i);
		    	    }
		    	}
			
		    	// try catch block
				try {
					// getting all sensor details
					String jsonString = server.readSensors();
					
					// JSON response convert to JAVA object
					final JSONArray arr = new JSONArray(jsonString);
					
					// for loop
					for(int i = 0; i < arr.length(); i++) {
						// declaring sensor's details to variables
						String status;
						String name = arr.getJSONObject(i).getString("name");
						int floorNumber = arr.getJSONObject(i).getInt("floorNumber");
						int roomNumber = arr.getJSONObject(i).getInt("roomNumber");
						int co2 = arr.getJSONObject(i).getInt("carbonDioxideLevel");
						int smokeLevel = arr.getJSONObject(i).getInt("smokeLevel");
						
						// condition
						if(co2 > 5 || smokeLevel > 5) {
							// if one of sensor's value greater than 5, the value of the status will activated
							status = "Activated";
						}else {
							// otherwise value of the status will normal
							status = "Normal";
						}
						
						// insert a row with data
						listTableModel.addRow(new Object[]{name, floorNumber, roomNumber, co2, smokeLevel, status,});
					}					
				} catch (RemoteException e) {
					// print error
					e.printStackTrace();
				}		    	
		    }
		},0,30000); // this will run after every 30 seconds
		
		// set values
		table_1 = new JTable(listTableModel);
		table_1.setEnabled(false);
		table_1.getColumnModel().getColumn(0).setPreferredWidth(83);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(85);
		scrollPane.setViewportView(table_1);

		// create label object and set values
		JLabel lblWelcome = new JLabel("Welcome");
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblWelcome.setBounds(365, 13, 101, 41);
		contentPane.add(lblWelcome);
	}
}
