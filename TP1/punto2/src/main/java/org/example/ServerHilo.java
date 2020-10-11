package org.example;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerHilo implements Runnable{
    public Socket cliente;

    public ServerHilo(Socket cliente){
        this.cliente=cliente;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.cliente.getInputStream()));
            PrintStream output = new PrintStream(cliente.getOutputStream(),true);



            Messagee mensajito = new Messagee(1,"el servidor se presenta");

            Gson gson = new Gson();

            String pruebaJson = gson.toJson(mensajito);

            output.println(pruebaJson);

            String messageInput = input.readLine();
            System.out.println(messageInput);
            this.cliente.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
