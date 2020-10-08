package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInt  extends Remote {
    public int[] sumarVector(int[] v1,int[] v2) throws RemoteException;
    public int[] restarVector(int[] v1,int[] v2) throws RemoteException;
    public int[] retornarVector(int[] v1) throws  RemoteException;

}
