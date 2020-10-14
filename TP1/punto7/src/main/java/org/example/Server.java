package org.example;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 * Hello world!
 *
 */
public class Server
{
    public static void main( String[] args )
    {

        try {
            Registry r = LocateRegistry.createRegistry(10000);

            ejecutarTarea et = new ejecutarTarea();
            Iejecutartarea iet = (Iejecutartarea) UnicastRemoteObject.exportObject(et,12000);
            r.rebind("ejecutorTarea", iet);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
