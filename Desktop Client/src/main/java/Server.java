// importing dependencies
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

// class definition with extends UnicastRemoteObject and implements LoginInterface, SensorInterface
public class Server extends UnicastRemoteObject implements LoginInterface,SensorInterface{

    // constructor
    public Server() throws RemoteException{
        super();
    }
    
    // main method
    public static void main(String[] args) {
        try {
        	// creating registry
            Registry reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            // creating server object
            Server obj = new Server();
            
            // bind the rmi server with server object
            reg.rebind("rmi://localhost/service", obj);
            
            // message that indicate when server running
            System.out.println("Server is running");
            
            // declare the timer
    		Timer t = new Timer();
    		
    		// set the schedule function and rate
    		t.scheduleAtFixedRate(new TimerTask() {

    		    @Override
    		    public void run() {

    				try {
    					// create server object
    					Server obj = new Server();
    					
    					// call the REST API and get the all sensor details and assign it to a variable
    					String jsonString = obj.readSensors();
    					
    					// convert to java JSON Array object
    			    	JSONArray arr = new JSONArray(jsonString);
    			    	
    			    		// loop
    						for(int i = 0; i < arr.length(); i++) {
    							// initializing carbon dioxide level
    							int co2 = arr.getJSONObject(i).getInt("carbonDioxideLevel");
    							
    							// initializing smoke level
    							int smokeLevel = arr.getJSONObject(i).getInt("smokeLevel");
    							
    							// condition
    							if(co2 > 5 || smokeLevel > 5) {
    								// if any sensor's carbon dioxide level or
    								// smoke level is grater than 5 these objects will fire.
    								new SendEmail();
    								new SendSMS();
    							}	
    						}		
	    				} catch (RemoteException e) {
	    					// print error
	    					e.printStackTrace();
	    				}
    		    	}			
    		},0,15000); // after every 15 seconds this will execute
        } catch (RemoteException ex) {
        	// print error
            System.out.println(ex.getMessage());
        }
    }

    // login function
    public String login(String username, String password) throws RemoteException {
    	// create client object
		Client client = Client.create();
		
		// create we resource object
		WebResource webResource;
		
		// declaring API URL
		webResource = client.resource("http://localhost:5000/api/users/login");
		
		// setting JSON type string before send it to the API
		String input = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
		
		// getting response
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
		
		// assigning that response to a string variable
		String output = response.getEntity(String.class);

		// return response
		return output;	
    }

	@Override
	public String createSensor(String name, int floorNumber, int roomNumber)
			throws RemoteException {
		// create client object
		Client client = Client.create();
		
		// create we resource object
		WebResource webResource;
		
		// declaring API URL
		webResource = client.resource("http://localhost:5000/api/sensors");
		
		// setting JSON type string before send it to the API
		String input = "{\"name\":\"" + name + "\",\"floorNumber\":" + floorNumber + ",\"roomNumber\":" + roomNumber + "}";
		
		// getting response
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);		
		
		// assigning that response to a string variable
		String output = response.getEntity(String.class);
		
		// return status
		return output;
	}

	@Override
	public String readSensors() throws RemoteException {
		// create client object
		Client client = Client.create();
		// create we resource object
		WebResource webResource;
		
		// declaring API URL
		webResource = client.resource("http://localhost:5000/api/sensors");
		
		// getting response
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		
		// condition
		if (response.getStatus() != 200) {
			   // if response status not equal to 200 then throw new error
			   throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		
		// assigning that response to a string variable
		String output = response.getEntity(String.class);
		
		// return response
		return output;
	}

	@Override
	public String readSensor(String id) throws RemoteException {
		// create client object
		Client client = Client.create();
		
		// create we resource object
		WebResource webResource;
		
		// declaring API URL
		webResource = client.resource("http://localhost:5000/api/sensors/" + id + "");
		
		// getting response
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		
		// condition
		if (response.getStatus() != 200) {
			   // if response status not equal to 200 then throw new error	 
			   throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		
		// assigning that response to a string variable 
		String output = response.getEntity(String.class);
		
		// return response
		return output;
	}

	@Override
	public String updateSensor(String id, String name, int floorNumber, int roomNumber) throws RemoteException {

		// create client object
		Client client = Client.create();
		
		// create we resource object
		WebResource webResource;

		// declaring API URL
		webResource = client.resource("http://localhost:5000/api/sensors/" + id + "");
		
		// setting JSON type string before send it to the API
		String input = "{\"name\":\"" + name + "\",\"floorNumber\":" + floorNumber + ",\"roomNumber\":" + roomNumber + "}";
		
		// getting response
		ClientResponse response = webResource.type("application/json").put(ClientResponse.class, input);
		
		// condition
		if (response.getStatus() != 200) {
			// if response status not equal to 200 then throw new error	 
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		// assigning that response to a string variable 
		String output = response.getEntity(String.class);
		
		// return response
		return output;
	}

	@Override
	public String deleteSensor(String id) throws RemoteException {
		// create client object
		Client client = Client.create();
		
		// create we resource object
		WebResource webResource;
		
		// declaring API URL
		webResource = client.resource("http://localhost:5000/api/sensors/" + id + "");

		// getting response
		ClientResponse response = webResource.type("application/json").delete(ClientResponse.class);
		
		// condition
		if (response.getStatus() != 200) {
			// if response status not equal to 200 then throw new error	 
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}
		
		// assigning that response to a string variable
		String output = response.getEntity(String.class);

		// return response
		return output;
	}   
}
