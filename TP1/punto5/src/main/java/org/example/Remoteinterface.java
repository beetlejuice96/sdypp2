package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Remoteinterface extends Remote {
    public String retornarClima(String ciudad) throws RemoteException;
}
