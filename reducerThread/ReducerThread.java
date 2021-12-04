package reducerThread;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import reducer.ReducerInterface;

public class ReducerThread  extends Thread {
    ReducerInterface ri;

    public ReducerThread(ReducerInterface r){
        this.ri = r;
    }

    public void run(){
        try {
            sleep(50);
            this.ri.reducing();
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}
