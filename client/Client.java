package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import manager.ManagerInterface;

public class Client {
    public static void main(String[] args){
        try{
            Registry registry = LocateRegistry.getRegistry(9000);

            ManagerInterface skeleton = (ManagerInterface) registry.lookup("manager");

            //String text = "Quand je te vois passer, ô ma chère indolente, Au chant des instruments qui se brise au plafond Suspendant ton allure harmonieuse et lente, Et promenant l'ennui de ton regard profond; Quand je contemple, aux feux du gaz qui le colore, Ton front pâle, embelli par un morbide attrait, Où les torches du soir allument une aurore, Et tes yeux attirants comme ceux d'un portrait, Je me dis: Qu'elle est belle! et bizarrement fraîche! Le souvenir massif, royale et lourde tour, La couronne, et son coeur, meurtri comme une pêche, Est mûr, comme son corps, pour le savant amour.";

            String text = "la femme de femme mon patron femme est la maitaissse de ma femme qui est l'amant de la femme de caroline du nord";
            System.out.println(skeleton.mapReduce(text));

            // clean the registry
            UnicastRemoteObject.unexportObject(registry,true);


        }
        catch (Exception e){
            System.err.println("Client exception: " + e.toString()); 
            e.printStackTrace(); 
        }
    }
}
