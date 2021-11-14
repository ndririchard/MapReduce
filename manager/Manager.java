package manager;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import mapper.Mapper;
import reducer.Reducer;
import mapper.MapperInterface;
import reducer.ReducerInterface;

public class Manager 
    extends UnicastRemoteObject
    implements ManagerInterface{
    public ArrayList<String> result;

    public Manager() throws RemoteException{
        super();
    }

    public Registry getAppRegistry() throws RemoteException{
        return LocateRegistry.getRegistry(9000);
    }

    public ArrayList<String> getResult(){
        return this.result;
    }

    public void setResult(ArrayList<String> r){
        this.result = r;
    }
    
    public ArrayList<String> splitThis(String text){
        ArrayList<String> result = new ArrayList<String>();
        text = text.replaceAll("[.()\'\"-,;_]"," ");
        String[] textS = text.split("\\s+");
        for (String txt:textS){
            result.add(txt);
        }
        return result;
    }
    
    public ArrayList<String> splitFile(String text){
        ArrayList<String> splittedFile = new ArrayList<String>();
        ArrayList<String> formatedData = splitThis(text);
        int numberOfSubText = 2;
        
        int start = 0;
        int step = formatedData.size()/numberOfSubText;
        int end = start + step;
        try{
            while (numberOfSubText != 0){
                String subText = "";
    
                for (int y=start; y<end; y++){
                    subText += formatedData.get(y) + " ";
                }
                splittedFile.add(subText);
                start = end +1;
                end = end + step;
                numberOfSubText--;
    
            }
        }catch(Exception e){
            // pass
        }
        
        return splittedFile;   
    }

    public synchronized void start_mapping(MapperInterface machine1, MapperInterface machine2, ArrayList<String> data){
        new Thread(){
            public void run(){
                try {
                    machine1.mapping(data.get(0));
                    //machine2.mapping(data.get(1));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        new Thread(){
            public void run(){
                try {
                    machine2.mapping(data.get(1));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
    }

    public ArrayList<String> mapReduce(String text) throws AccessException, RemoteException, NotBoundException{
        /*--------------step1 : split file--------------*/
        ArrayList<String> data = splitFile(text);

        // select machinses which are able to do maping
        MapperInterface mapper1 = (MapperInterface) getAppRegistry().lookup("mapper1");
        MapperInterface mapper2 = (MapperInterface) getAppRegistry().lookup("mapper2");

        /*--------------step2 : mapping-----------------*/
        
        // mapper1.mapping(data.get(0));
        // mapper2.mapping(data.get(1));
        start_mapping(mapper1, mapper2, data);

        /*------step2-3 : shuffling and reducing--------*/
        ReducerInterface reducer = (ReducerInterface) getAppRegistry().lookup("reducer");

        reducer.reducing();
        //----------------------------------------------
        return reducer.getResult();
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException{
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
