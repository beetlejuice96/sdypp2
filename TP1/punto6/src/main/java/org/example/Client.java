package org.example;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main( String[] args )
    {

        try {
            Registry client = LocateRegistry.getRegistry("127.0.0.1",10000);
            String[] servicios = client.list();
            RemoteInt ri = (RemoteInt) client.lookup(servicios[0]);

            //genero los dos vectores
            int[] v1 = {1,2,3,4,5,6,7,8,9,10};
            int[] v2 = {1,2,3,4,5,6,7,8,9,10};
            System.out.println(mostrarv(v1,1));
            System.out.println(mostrarv(v2,2));

            int[] v3 = ri.sumarVector(v1,v2);
            System.out.println("El resultado de la suma es = ");
            System.out.println(mostrarv(v3,3));
            //System.out.println(mostrarv(v1,1));
            //System.out.println(mostrarv(v2,2));

            //llamoa la sima
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

    public static String mostrarv (int[] v,int i){
        String vector ="vector"+i+" ";
        for (int k=0; k<v.length;k++){
            vector+= v[k]+"-";

        }
        return  vector;
    }


}
