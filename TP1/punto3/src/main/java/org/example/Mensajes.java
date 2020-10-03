package org.example;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Mensajes  {
    private Socket cliente;

    private List<Mensaje> msj;

    public Mensajes(){
        this.msj = new ArrayList<Mensaje>();
    }

    public List<Mensaje> getMensajePara(String destino){
        List<Mensaje> rta = new ArrayList<Mensaje>();
        this.msj.forEach(m -> {
            if (m.getDestino().equals(destino)){
                rta.add(m);
            }
        });

        return rta;
    }

    public void add(Mensaje m){
            this.msj.add(m);
    }

}
