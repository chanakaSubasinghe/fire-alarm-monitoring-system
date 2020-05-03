// importing dependencies
import java.rmi.Remote;
import java.rmi.RemoteException;

// class definition
public interface SensorInterface extends Remote{
	// function for create sensor
	public String createSensor(String name,int floorNumber,int roomNumber) throws RemoteException;
	
	// function for read all sensors
	public String readSensors() throws RemoteException;
	
	// function for read only one sensor
	public String readSensor(String id) throws RemoteException;
	
	// function for update sensor using _id
	public String updateSensor(String id,String name,int floorNumber,int roomNumber) throws RemoteException;
	
	// function for delete sensor
	public String deleteSensor(String id) throws RemoteException;
}
