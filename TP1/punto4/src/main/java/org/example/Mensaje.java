package org.example;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private int id;

    private String origen;

    private String destino;

    private String cuerpo;

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String msg = "Origen: " + this.origen + "\n";
        msg+=("Destino: " + this.destino + "\n");
        msg+=("Mensaje: " + this.cuerpo+ "\n");
        return msg;
    }
}
