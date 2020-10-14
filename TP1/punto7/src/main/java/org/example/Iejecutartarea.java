package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Iejecutartarea extends Remote {

    public int ejecutar(Tarea t) throws RemoteException;
}
