// importing dependencies
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;

// class definition
public class LoginFrame extends JFrame implements ActionListener {

	// creating necessary objects and set values
    JPanel panel;
    JLabel user_label, password_label, message;
    static JTextField userName_text;
    static JTextField password_text;
    JButton submit, cancel;

    // constructor
    LoginFrame() {
    	// se values
    	setBackground(new Color(204, 51, 0));
        
        // User Label and set values
        user_label = new JLabel();
        user_label.setFont(new Font("Tahoma", Font.PLAIN, 18));
        user_label.setText("         User Name :");
        userName_text = new JTextField();
        
        // Password and set values
        password_label = new JLabel();
        password_label.setFont(new Font("Tahoma", Font.PLAIN, 18));
        password_label.setText("           Password :");
        password_text = new JPasswordField();

        // Submit and set values
        submit = new JButton("Login");
        submit.setForeground(new Color(255, 255, 240));
        submit.setBackground(new Color(51, 102, 255));
        submit.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));

        // create new panel object
        panel = new JPanel(new GridLayout(3, 1));

        // set values
        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);

        // set values
        message = new JLabel();
        message.setForeground(new Color(255, 102, 0));
        message.setBackground(new Color(255, 102, 51));
        panel.add(message);
        panel.add(submit);
        
        // Adding the listeners to components..
        submit.addActionListener(this);
        getContentPane().add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(409, 170);
        setVisible(true);
        setLocation(600,290);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	ClientDashboard ds;
				try {
					ds = new ClientDashboard();
					ds.setVisible(true);
				} catch (NotBoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}					
            }
        });
    }

    // main function
    public static void main(String[] args) {new LoginFrame();}

    // when user click submit button this function will start to execute
	public void actionPerformed(ActionEvent e) {
		// try-catch block
		try {
				// create a registry and looking for RMI server
				Registry reg = LocateRegistry.getRegistry("localhost", 1099);
				
				// create an object of login interface
				LoginInterface server = (LoginInterface) reg.lookup("rmi://localhost/service");
									
				// get user input
				String username = userName_text.getText();
				String password = password_text.getText();
				
				// call the login function and get response
				String response = server.login(username, password);

				// condition
				if(response.equals("false")) {
					// if invalid credentials, then prompt a message to user
					JOptionPane.showMessageDialog(null, "username or password incorrect!");
				}
				else {
					// if valid credentials prompt a message 
					JOptionPane.showMessageDialog(null, "successfully logged in.");
					
					// show administrator dashBaord to user
					AdminDashboard regFace =new AdminDashboard();
					// start to visible 
					regFace.setVisible(true);
					dispose();						
				}
			} catch (RemoteException | NotBoundException ex) {
				// print error
				System.out.println(ex.getMessage());
			}
	}			
}
