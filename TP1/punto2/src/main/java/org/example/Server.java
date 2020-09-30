package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Server
{
    String nombre;


    public Server (String nombre){
        this.nombre=nombre;
    }

    public static void main( String[] args )
    {

        try {
            //primer paso- instanciar el servidor
            ServerSocket ss = new ServerSocket(9000);

            //segundo paso
            while (true){
                Socket cliente = ss.accept();
                System.out.println("Cliente recibido"+cliente.getPort());

                ServerHilo sh = new ServerHilo(cliente);
                Thread t = new Thread(sh);

                t.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
