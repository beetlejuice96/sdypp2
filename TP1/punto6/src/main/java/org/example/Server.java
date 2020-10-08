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
            //paso1 crear RMI server
            Registry serverRMI = LocateRegistry.createRegistry(10000);
            ServerImp s1 = new ServerImp();
            RemoteInt servicio = (RemoteInt) UnicastRemoteObject.exportObject(s1,8000);
            serverRMI.rebind("vector-s1",servicio);

        }catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
