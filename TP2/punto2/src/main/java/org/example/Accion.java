package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Accion implements Runnable {
    private ServidorDeposito server;
    private String cuenta;
    private Socket cliente;

    private BufferedReader reader;
    private PrintWriter printer;

    public Accion(String cuenta, Socket cliente, ServidorDeposito serverD){
        this.server = serverD;
        this.cliente = cliente;
        this.cuenta = cuenta;

        try {
            reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            printer = new PrintWriter(cliente.getOutputStream(),true);
            //writer= new BufferedWriter(new FileWriter(cuenta));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

    }
}
