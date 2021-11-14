package server;

import java.nio.channels.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.registry.LocateRegistry;
import manager.Manager;
import manager.ManagerInterface;
import mapper.Mapper;
import mapper.MapperInterface;
import reducer.Reducer;
import reducer.ReducerInterface;

public abstract class Server {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, java.rmi.AlreadyBoundException{
        /*------------------create a registry---------------------*/

        // our registry is listenning the port 9000
        Registry registry = LocateRegistry.createRegistry(9000);

        /*-------------------------------------------------------*/

        // here we will Instantiate our remote classes
        Manager manager = new Manager();
        Mapper mapper1 = new Mapper();
        Mapper mapper2 = new Mapper();
        Reducer reducer = new Reducer();

        /*-------------------------------------------------------*/

        // now here we are exporting the remote object to his stub
        ManagerInterface stub_manager = (ManagerInterface) UnicastRemoteObject.exportObject(manager, 9000);
        MapperInterface stub_mapper1 = (MapperInterface) UnicastRemoteObject.exportObject(mapper1, 9000);
        MapperInterface stub_mapper2 = (MapperInterface) UnicastRemoteObject.exportObject(mapper2, 9000);
        ReducerInterface stub_reducer = (ReducerInterface) UnicastRemoteObject.exportObject(reducer, 9000);

        /*-------------------------------------------------------*/
        // Binding the remote object in the registry

        registry.bind("manager",stub_manager);
        registry.bind("mapper1",stub_mapper1);
        registry.bind("mapper2",stub_mapper2);
        registry.bind("reducer",stub_reducer);

        System.err.println("Manager is ready"); 

    }
    
}
