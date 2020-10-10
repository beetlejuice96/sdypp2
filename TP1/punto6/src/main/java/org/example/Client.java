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

            //provoco el error intencional
            //int[] v4= ri.ErrorIntencional(v1);
            //System.out.println("error intencional en el vector 1: "+mostrarv(v4,4));

            // provoco error intencional 2
            System.out.println("PROVOCANDO ERROR 2");
            Vector vector1 = new Vector();
            vector1.setV(v1);
            Vector aux = ri.errorIntencional2(vector1);
            System.out.println("vector antes de provocar error: "+mostrarv(vector1.getV(), 1));
            System.out.println("error intencional 2 en el vector 1: "+mostrarv(aux.getV(),1));


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
