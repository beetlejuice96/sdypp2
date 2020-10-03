package org.example;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Server
{
    public static void main( String[] args ) {

        try {
            ServerSocket ss = new ServerSocket(18000);
            System.out.println("servidor iniciado correctamente");

            JOptionPane.showMessageDialog(null,"Servidor iniciado");

            Mensajes lista = new Mensajes();

            while (true){
                Socket client = ss.accept();
                System.out.println("Spcket aceptado "+client.getPort());
                AdminMensajes admin = new AdminMensajes(client,lista);

                Thread hilo = new Thread(admin);

                hilo.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("un error detuvo el funcionamiento");
        }


    }
}

