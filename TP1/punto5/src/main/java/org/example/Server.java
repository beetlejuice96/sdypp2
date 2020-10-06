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

        try{
            Registry serverRMI = LocateRegistry.createRegistry(9000);

            ServerImp server= new ServerImp();

            Remoteinterface serviceA = (Remoteinterface) UnicastRemoteObject.exportObject(server,10000);

            serverRMI.rebind("servicio",serviceA);

            System.out.println("Servidor ok");

        }catch (RemoteException e){
            e.printStackTrace();
        }


    }
}
