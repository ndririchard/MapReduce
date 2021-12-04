package mapper;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Mapper
    implements MapperInterface{
    public ArrayList<String> result;

    public Mapper() throws RemoteException{
        super();
    }

    public ArrayList<String> getResult(){
        return this.result;
    }

    public void setResult(ArrayList<String> r){
        this.result = r;
    }

    public boolean compareString(String string1, String string2){
        boolean result = false;

        if (string1.length() != string2.length()){
            result = false;
        }
        else {
            int nb = 0;
            for (int index = 0; index < string1.length(); index++){
                if (string1.charAt(index) == string2.charAt(index)){
                    nb ++;
                }
            }
            if (nb == string1.length()){
                result = true;
            }
        }

        return result;

    }

    public void mapping(String text){
        System.out.println("i am starting...");
        ArrayList<String> mappingResult = new ArrayList<String>();
        ArrayList<String> splittedList = new ArrayList<String>();

        text = text.toLowerCase();
        String[] convertTextToList = text.split("\\s+");
        for (String string:convertTextToList){
            splittedList.add(string);
        }

        while(splittedList.size() != 0){
            String selectedWord = splittedList.get(0);
            //ArrayList<Integer> index = new ArrayList<Integer>();
            int counter = 0;
            
            for (int k = 0; k < splittedList.size(); k++){
                if (compareString(selectedWord, splittedList.get(k))){
                    counter++;
                    splittedList.remove(k);
                }
            }
            
            mappingResult.add(selectedWord + ":" + Integer.toString(counter));       
        }
        
        setResult(mappingResult); 
        System.out.println("i finished...");     
    }
}
