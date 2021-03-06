package reducer;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.ArrayList;


import mapper.MapperInterface;

public class Reducer
    implements ReducerInterface {
    private ArrayList<String> result = new ArrayList<String>();
    private ArrayList<MapperInterface> list = new ArrayList<MapperInterface>();


    public Reducer() throws RemoteException{
        super();
    }

    public ArrayList<String> getResult(){
        return this.result;
    }

    public void addThisMapper(MapperInterface mi){
        this.list.add(mi);
    }

    public void setResult(ArrayList<String> r){
        this.result = r;
    }

    public Registry getAppRegistry() throws RemoteException{
        return LocateRegistry.getRegistry(9000);
    }

    public boolean compareString(String str1, String str2) throws AccessException, RemoteException, NotBoundException{
        // this invoke a methode from the object mapper
        MapperInterface mapper = (MapperInterface) getAppRegistry().lookup("mapper1");
        return mapper.compareString(str1, str2);
    }

    public ArrayList<String> concateList(ArrayList<String> list1, ArrayList<String> list2){
        for (String wrd:list2){
            list1.add(wrd);
        }
        return list1;
    }

    public ArrayList<String> collectDataFromMapper() throws AccessException, RemoteException, NotBoundException{
        ArrayList<String> array = new ArrayList<String>();
        for (MapperInterface mi:this.list){
            concateList(array, mi.getResult());
        }
        this.list.clear();
        return array;
    }


    public void reducing() throws AccessException, RemoteException, NotBoundException{
        // here we collect the result of the mapping
        ArrayList<String> suffList = collectDataFromMapper();
        
        
        ArrayList<String> reduicingResult = new ArrayList<String>();

        while (suffList.size() != 0){
            String[] selectedItem = suffList.get(0).split(":");
            int finalCounter = 0;

            for (int j=0; j<suffList.size(); j++){
                String[] tempSelection = suffList.get(j).split(":");
                try {
                    if (compareString(selectedItem[0], tempSelection[0])){
                        finalCounter += Integer.parseInt(tempSelection[1]);
                        suffList.remove(j);
                    }
                } catch (NumberFormatException | NotBoundException e) {
                    e.printStackTrace();
                }
            }
            reduicingResult.add(selectedItem[0] + ":" +Integer.toString(finalCounter));
        }
        
        
        setResult(reduicingResult);
    }
}
