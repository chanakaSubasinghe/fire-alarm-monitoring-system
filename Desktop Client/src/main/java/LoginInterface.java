// importing dependencies
import java.rmi.Remote;
import java.rmi.RemoteException;

// class definition
public interface LoginInterface extends Remote{
	// login function
    public String login(String username, String password) throws RemoteException;
}
