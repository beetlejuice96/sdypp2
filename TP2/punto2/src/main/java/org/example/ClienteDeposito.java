package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClienteDeposito {
    private  int sport;
    private BufferedReader reader;
    private PrintWriter writer;



    public void deposito() throws IOException {

        //seteo las herramientas que voy a usar para comunicarme con el servidor.
        Socket s = new Socket("127.0.0.1",this.sport);
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        writer = new PrintWriter(s.getOutputStream());

        System.out.println("Server : "+reader.readLine());

        double montoDeposito = (new Random().nextInt(50) + 50)*10;
        System.out.println( "El valor a depositar es : "+montoDeposito);

        writer.println(montoDeposito);
        writer.flush();
        System.out.println("Server : "+reader.readLine());
        System.out.println("Server : "+reader.readLine());
        s.close();
    }
    public  ClienteDeposito(int puertoServer){
        this.sport=puertoServer;
    }

    public static void main(String[] args) throws IOException {
        ClienteDeposito cd = new ClienteDeposito(9000);
        cd.deposito();
    }
}
