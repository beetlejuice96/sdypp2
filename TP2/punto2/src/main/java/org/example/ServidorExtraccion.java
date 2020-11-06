package org.example;

import org.graalvm.compiler.hotspot.nodes.VMErrorNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServidorExtraccion {

private String cuenta;
private Logger logger;

    public ServidorExtraccion(String cuenta,String loggerName, String logFile){
        this.logger = Logger.getLogger(loggerName);
        this.cuenta = cuenta;
        try{
            FileHandler handler = new FileHandler(logFile);
            handler.setFormatter(new SimpleFormatter());
            this.logger.addHandler(handler);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void run(){
        try{
            ServerSocket servidor = new ServerSocket(10000);
            while (true){
                Socket cliente = servidor.accept();
                ExtraccionC server_extraccion = new ExtraccionC(cuenta,cliente,this);
                Thread nuevoHilo = new Thread( server_extraccion);
                nuevoHilo.start();
            }

        }catch (IOException e){

        }

    }

    public void logeo (String msj){
        long mlseg = System.currentTimeMillis();
        msj = msj + "|| Tiempo transcurrido => "+mlseg;
        this.logger.log(Level.INFO,msj);
    }


    public static void main(String[] args){
    }
}
