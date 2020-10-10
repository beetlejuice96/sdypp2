package org.example;

import java.rmi.RemoteException;

public class ServerImp  implements RemoteInt{


    @Override
    public int[] sumarVector(int[] v1, int[] v2) throws RemoteException {
        int[] v3 = new int[10];
        for (int i =0;i<v1.length;i++){
            v3[i]= v1[i]+v2[i];

        }
        return v3;
    }

    @Override
    public int[] restarVector(int[] v1, int[] v2) throws RemoteException {
        int[] v3 = new int[10];
        for (int i =0;i<v1.length;i++){
            v3[i]= v1[i]-v2[i];

        }
        return v3;
    }

    @Override
    public int[] retornarVector(int[] v1) throws RemoteException {
        return v1;
    }

    @Override
    public int[] ErrorIntencional(int[] v1) throws RemoteException {
        //int length = Integer.min(v1.length,v2.length);
        for(int i =0;i<v1.length;i++ ){
            v1[i] = 0;
        }
        return v1;
    }

    @Override
    public Vector errorIntencional2(Vector v) throws RemoteException {
        //int[] vaux= new int[v.getV().length];
        for (int i = 0; i<v.getV().length;i++){
            //vaux[i]=v.getV()[i]+2;
            v.getV()[i]=0;
        }

        return v;
    }
}
