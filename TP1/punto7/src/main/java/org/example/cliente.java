package org.example;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class cliente {


    public static void main( String[] args )
    {
        try {
            Registry r = LocateRegistry.getRegistry("127.0.0.1",10000);
            Iejecutartarea iet = (Iejecutartarea) r.lookup("ejecutorTarea");

            Tarea t = new Tarea() {

                public int ejecutar() {
                    Random r = new Random();

                    return r.nextInt(10000)*r.nextInt(100000);
                }
            };
            System.out.println("Número aleatorio es: " + iet.ejecutar(t));
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}
