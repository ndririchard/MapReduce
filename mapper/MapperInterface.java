package mapper;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MapperInterface extends Remote{

    ArrayList<String> getResult() throws RemoteException;
    boolean compareString(String string1, String string2) throws RemoteException;
    // mapper are machines that can do the mapping
    void mapping(String text) throws RemoteException;
  
}
