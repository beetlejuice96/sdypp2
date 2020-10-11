package org.example;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server(int port) {

        try{
            ServerSocket ss = new ServerSocket(port);

            System.out.println("El servidor se inicio en el puerto -> "+port);

            while (true){
                Socket client = ss.accept();

                System.out.println("Cliente Aceptado : " + client.getInetAddress().getCanonicalHostName() + "Puerto : " + client.getPort()); //cliente devuelve su ip
                BufferedReader input = new BufferedReader(new InputStreamReader( client.getInputStream()));


                PrintWriter output = new PrintWriter( client.getOutputStream(),true ); //el true como lo especifica la ayuda de intellij es un autoflush.

                output.println("el servidor manda mensaje al cliente");
                System.out.println(input.readLine());
                client.close();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
            int port = 9000;
            Server  server = new Server(port);
    }
}
