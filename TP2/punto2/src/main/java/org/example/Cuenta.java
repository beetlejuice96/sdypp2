package org.example;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class Cuenta {

    private BufferedReader reader;
    private PrintWriter writer;



    public Cuenta(String path) {

    }


    public double getSaldo (){
        double value = 0;
        //logica para leer el saldo de la cuenta
        return value;
    }

    public double depositar(Double deposito){
        //logica para depositar un monto

        return getSaldo();
    }



    public double extraer(Double extraccion){
        //logica para extraccion

        return getSaldo();
    }



}
