package manager;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;



public interface ManagerInterface extends Remote {

    ArrayList<String> splitFile(String text) throws RemoteException, Exception;
    ArrayList<String> mapReduce(String text) throws RemoteException,NotBoundException;

}
