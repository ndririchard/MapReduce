package mapperThread;

import java.rmi.RemoteException;

import mapper.MapperInterface;

public class MapperThread extends Thread{
    private String d;
    private MapperInterface m;

    public MapperThread(MapperInterface m, String data){
        this.m = m;
        this.d = data;
    }

    public void run(){
        try {
            sleep(50);
            this.m.mapping(this.d);
            
        } catch (InterruptedException | RemoteException e) {
            e.printStackTrace();
        }
        
    }
}