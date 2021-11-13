package reducer;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ReducerInterface extends Remote {
    
    ArrayList<String> getResult() throws RemoteException;
    void reducing() throws RemoteException , NotBoundException;
}
