package reducer;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import mapper.MapperInterface;

public interface ReducerInterface extends Remote {
    
    ArrayList<String> getResult() throws RemoteException;
    void reducing() throws RemoteException , NotBoundException;
    void addThisMapper(MapperInterface mi) throws RemoteException;;
    ArrayList<String> concateList(ArrayList<String> list1, ArrayList<String> list2) throws RemoteException;
}
