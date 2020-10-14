package org.example;

import java.rmi.RemoteException;

public class ejecutarTarea implements Iejecutartarea{


    @Override
    public int ejecutar(Tarea t) throws RemoteException {
        return t.ejecutar();
    }
}
