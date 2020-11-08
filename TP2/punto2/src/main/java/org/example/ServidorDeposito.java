package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServidorDeposito {
    private Logger logger;
    private String cuenta;

    public ServidorDeposito(String cuenta,String loggerName, String logFile){
        this.logger = Logger.getLogger(loggerName);
        this.cuenta= cuenta;
        try {
            FileHandler handler = new FileHandler(logFile);
            handler.setFormatter(new SimpleFormatter());
            this.logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String [] args){
        System.out.println("Servidor de Depositos up");
        ServidorDeposito sd = new ServidorDeposito("C:\\Users\\dilan\\IdeaProjects\\ssydp2\\TP2\\cuenta.txt","logDepositos.txt","logger");
        sd.run();

    }


    public void run(){
        try {
            ServerSocket servidor = new ServerSocket(9000);
            while (true){
                Socket cliente = servidor.accept();
                DepositoC server_deposito = new DepositoC(cuenta,cliente,this);
                Thread nuevoHilo = new Thread(server_deposito);
                nuevoHilo.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void logeo(String msj){
        long mlseg = System.currentTimeMillis();
        msj = msj + "|| Tiempo transcurrido => "+mlseg;
        this.logger.log(Level.INFO,msj);

    }


}
