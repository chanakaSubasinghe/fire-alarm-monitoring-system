// importing dependencies
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.json.JSONArray;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
// class definition
public class AdminDashboard extends JFrame {

	// declaring variables
	private JPanel contentPane;
	private JTable table;
	private static ArrayList<String> ids = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	
	// main function
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// create new Administrator dashBoard object
					AdminDashboard frame = new AdminDashboard();
					
					// start to visible
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
	 * @throws RemoteException 
	 * @throws NotBoundException 
	 */
	// AdminDashboard function
	public AdminDashboard() throws RemoteException, NotBoundException {
		// set values
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 897, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddSensor ds = new AddSensor();	
				ds.setVisible(true);
			
			}	
		});
		btnNewButton.setFont(new Font("Arial Black", Font.BOLD, 14));
		btnNewButton.setBackground(UIManager.getColor("Button.light"));
		btnNewButton.setBounds(39, 56, 161, 41);
		contentPane.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(89, 137, 680, 335);
		contentPane.add(scrollPane);
		
		DefaultTableModel dm = new DefaultTableModel();
		table = new JTable(dm);
		table.setShowHorizontalLines(false);
		table.setEnabled(true);
		table.setRowSelectionAllowed(false);
		table.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		
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
		final Object[] columnNames = {"Sensor Name", "Floor Number", "Room Number", "CO2 Level", "Smoke Level", "Status", "Edit"};


		// creating table object
		final DefaultTableModel listTableModel;
		
		// parse declared row data and columns
		table.setModel(listTableModel = new DefaultTableModel(
				// data
				rowData,
				columnNames
			) {
				boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false, true
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});

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
						ids.add(arr.getJSONObject(i).getString("_id"));
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
						listTableModel.addRow(new Object[]{name, floorNumber, roomNumber, co2, smokeLevel, status,"Edit"});
					}					
				} catch (RemoteException e) {
					// print error
					e.printStackTrace();
				}		    	
		    }
		},0,30000); // this will run after every 30 seconds
		
		
		// set values
		table = new JTable(listTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(83);
		table.getColumnModel().getColumn(4).setPreferredWidth(85);
	    table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
	    table.getColumn("Edit").setCellEditor(
	    new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(table);
		
		
		// set values
		JLabel lblWelcome = new JLabel("Administrator");
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 21));
		lblWelcome.setBounds(381, 13, 151, 41);
		contentPane.add(lblWelcome);		
	}
	// class definition
	class ButtonRenderer extends JButton implements TableCellRenderer {

		  // constructor
		  public ButtonRenderer() {
		    setOpaque(true);
		  }

		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		    if (isSelected) {
		      setForeground(table.getSelectionForeground());
		      setBackground(table.getSelectionBackground());
		    } else {
		      setForeground(table.getForeground());
		      setBackground(UIManager.getColor("Button.background"));
		    }
		    setText((value == null) ? "" : value.toString());
		    return this;
		  }
		}

		// class definition
		class ButtonEditor extends DefaultCellEditor {
		  
		  // declaring variables	
		  protected JButton button;
		  private String label;

		  // function
		  public ButtonEditor(JCheckBox checkBox) {
		    super(checkBox);
		    button = new JButton();
		    button.setOpaque(true);
		    button.addActionListener(new ActionListener() {
		     

		    // action performed	function
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int index = table.getSelectedRow();

				// create new table model
		        TableModel model = table.getModel();
		        
		        // getting values from row
		        String SensorName = model.getValueAt(index, 0).toString();
		        String FloorNumber = model.getValueAt(index, 1).toString();
		        String RoomNumber = model.getValueAt(index, 2).toString();
		        
		        // call the edit sensor class and its constructor
				EditSensor regFace =new EditSensor(ids.get(index),SensorName,FloorNumber, RoomNumber);				
				regFace.setVisible(true);
			}
		    });
		  }

		  // get table details function
		  public Component getTableCellEditorComponent(JTable table, Object value,
			    // declaring variables
				boolean isSelected, int row, int column) {
			  
			    // condition
			    if (isSelected) {
			      button.setForeground(table.getSelectionForeground());
			      button.setBackground(table.getSelectionBackground());
			    } else {
			      button.setForeground(table.getForeground());
			      button.setBackground(table.getBackground());
			    }
			    
			    //  condition
			    label = (value == null) ? "" : value.toString();
			    button.setText(label);
			    return button;
			  }
	}
}

