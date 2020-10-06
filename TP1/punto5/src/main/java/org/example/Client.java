package org.example;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client{


    public static void main( String[] args )
    {
        Registry clientRmi;
        try{
            clientRmi = LocateRegistry.getRegistry("127.0.0.1", 9000);
            String[] services = clientRmi.list();

            Remoteinterface rem  =(Remoteinterface) clientRmi.lookup(services[0]);
            System.out.println("introduzca la ciudad");
            Scanner ciudad = new Scanner(System.in);
            String ciudadEntrada = ciudad.nextLine();

            String respuestaserver = rem.retornarClima(ciudadEntrada);
            System.out.println(respuestaserver);

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

}
