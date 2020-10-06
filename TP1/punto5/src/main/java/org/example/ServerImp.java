package org.example;

import java.rmi.RemoteException;

public class ServerImp implements Remoteinterface{

    @Override
    public String retornarClima(String ciudad) throws RemoteException {
        double temperatura = (Math.random()*40);
        String msj  ="La temperatura de la ciudad es "+ (String)ciudad+" es de "+ temperatura+" grados";
        return msj;
    }

}
