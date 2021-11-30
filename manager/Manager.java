package manager;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.regex.Pattern;

import mapper.Mapper;
import reducer.Reducer;
import mapper.MapperInterface;
import reducer.ReducerInterface;

public class Manager
    implements ManagerInterface{
    private int nb_mapper;
    private ArrayList<MapperInterface> selected_mappers;
    private ArrayList<ReducerInterface> selected_reducers;
    public ArrayList<String> result;


    public Manager() throws RemoteException{
        super();
    }

    public int getNbMapper(){
        return this.nb_mapper;
    }

    public void setNbMapper(int v){
        this.nb_mapper=v;
    }

    public ArrayList<MapperInterface> getSelectedMappers(){
        return this.selected_mappers;
    }

    public void addMapper(MapperInterface map){
        this.getSelectedMappers().add(map);
    }

    public ArrayList<ReducerInterface> getSelectedReducer(){
        return this.selected_reducers;
    }

    public void addReducer(ReducerInterface red){
        this.getSelectedReducer().add(red);
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

    public String deleteAccent(String text){
        String strTemp = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(strTemp).replaceAll("");
    }

    public ArrayList<String> splitThis(String text){
        ArrayList<String> result = new ArrayList<String>();
        text = deleteAccent(text);
        text = text.replaceAll("[^a-zA-Z0-9\\s]","");
        text = text.replaceAll("[\\s]","-_-");
        String[] textS = text.split("-_-");
        for (String txt:textS){
            result.add(txt);
        }
        return result;
    }
    
    public ArrayList<String> splitFile(String text){

        ArrayList<String> splittedFile = new ArrayList<String>();
        ArrayList<String> formatedData = splitThis(text);

        int numberOfSubText = nb_machine_acc_data_size(formatedData);
        
        int start = 0;
        int step = formatedData.size()/numberOfSubText;

        int proof_reader = formatedData.size() % numberOfSubText;
        int end = start + step + proof_reader;
        try{
            while (numberOfSubText != 0){
                String subText = "";
                

                for (int y=start; y<end; y++){
                    subText += formatedData.get(y) + " ";
                }

                splittedFile.add(subText);
                start = end;
                end = start + step;
                numberOfSubText--;
            }
        }catch(IndexOutOfBoundsException e){
            // pass
        }
        
        return splittedFile;   
    }

    public synchronized void start_mapping(MapperInterface machine1, MapperInterface machine2, ArrayList<String> data){
        Thread t1 = new Thread(){
            public void run(){
                try {

                    machine1.mapping(data.get(0));

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        
        Thread t2 = new Thread(){
            public void run(){
                try {

                    machine2.mapping(data.get(1));

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();

        try {

            t1.join();
            t2.join();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public int nb_machine_acc_data_size(ArrayList<String> data){
        int r, d_size;
        r = 2;
        d_size = data.size();
        
        if (d_size>=0 && d_size<1000){
            r = 2;
        }

        if (d_size>=1000 && d_size<10000){
            r = 4;
        }

        if (d_size>=10000){
            r = 6;
        }

        return r;

    }

    public int nb_reducer(int val){
        return val/2;
    }

    public ArrayList<String> mapReduce(String text) throws AccessException, RemoteException, NotBoundException{
        /*--------------step1 : split file--------------*/
        ArrayList<String> data = splitFile(text);

        // select machinses which are able to do maping
        MapperInterface mapper1 = (MapperInterface) getAppRegistry().lookup("mapper1");
        MapperInterface mapper2 = (MapperInterface) getAppRegistry().lookup("mapper2");
        MapperInterface mapper3 = (MapperInterface) getAppRegistry().lookup("mapper3");

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
        //UnicastRemoteObject.unexportObject(registry, true);

        /*-------------------------------------------------------*/

        // here we will Instantiate our remote classes
        Manager manager = new Manager();
        Mapper mapper1 = new Mapper();
        Mapper mapper2 = new Mapper();
        Mapper mapper3 = new Mapper();
        Mapper mapper4 = new Mapper();
        Mapper mapper5 = new Mapper();
        Mapper mapper6 = new Mapper();
        Reducer reducer1 = new Reducer();
        Reducer reducer2 = new Reducer();
        Reducer reducer3 = new Reducer();

        /*-------------------------------------------------------*/

        // now here we are exporting the remote object to his stub
        ManagerInterface stub_manager = (ManagerInterface) UnicastRemoteObject.exportObject(manager, 9000);
        MapperInterface stub_mapper1 = (MapperInterface) UnicastRemoteObject.exportObject(mapper1, 9000);
        MapperInterface stub_mapper2 = (MapperInterface) UnicastRemoteObject.exportObject(mapper2, 9000);
        MapperInterface stub_mapper3 = (MapperInterface) UnicastRemoteObject.exportObject(mapper3, 9000);
        MapperInterface stub_mapper4 = (MapperInterface) UnicastRemoteObject.exportObject(mapper4, 9000);
        MapperInterface stub_mapper5 = (MapperInterface) UnicastRemoteObject.exportObject(mapper5, 9000);
        MapperInterface stub_mapper6 = (MapperInterface) UnicastRemoteObject.exportObject(mapper6, 9000);
        ReducerInterface stub_reducer1 = (ReducerInterface) UnicastRemoteObject.exportObject(reducer1, 9000);
        ReducerInterface stub_reducer2 = (ReducerInterface) UnicastRemoteObject.exportObject(reducer2, 9000);
        ReducerInterface stub_reducer3 = (ReducerInterface) UnicastRemoteObject.exportObject(reducer3, 9000);

        /*-------------------------------------------------------*/
        // Binding the remote object in the registry

        registry.bind("manager",stub_manager);
        registry.bind("mapper1",stub_mapper1);
        registry.bind("mapper2",stub_mapper2);
        registry.bind("mapper3",stub_mapper3);
        registry.bind("mapper4",stub_mapper4);
        registry.bind("mapper5",stub_mapper5);
        registry.bind("mapper6",stub_mapper6);
        registry.bind("reducer1",stub_reducer1);
        registry.bind("reducer2",stub_reducer2);
        registry.bind("reducer3",stub_reducer3);

        System.err.println("Manager is ready"); 
    }
}
