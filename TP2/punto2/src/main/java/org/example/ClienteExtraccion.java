package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClienteExtraccion {

    private int sport;
    private BufferedReader reader;
    private PrintWriter writer;


    public void extraccion() throws IOException {

        //seteo las herramientas que voy a usar para comunicarme con el servidor.
        Socket s = new Socket("127.0.0.1",this.sport);
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        writer = new PrintWriter(s.getOutputStream());

        System.out.println("Server : "+reader.readLine());

        double montoExtraccion = (new Random().nextInt(50) + 50)*10;
        System.out.println( "El valor a extraer es : "+montoExtraccion);

        writer.println(montoExtraccion);
        writer.flush();
        System.out.println("Server : "+reader.readLine());
        System.out.println("Server : "+reader.readLine());
        s.close();
    }

    public  ClienteExtraccion(int puertoServer){
        this.sport=puertoServer;
    }

    public static void main(String[] args) throws IOException {
        ClienteExtraccion ce = new ClienteExtraccion(10000);
        ce.extraccion();
    }


}
