package org.example;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServidorDeposito {
    private Logger logger;

    public ServidorDeposito(String loggerName, String logFile){
        this.logger = Logger.getLogger(loggerName);
        try {
            FileHandler handler = new FileHandler(logFile);
            handler.setFormatter(new SimpleFormatter());
            this.logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args){

    }
    public void run(){

    }

    public void logeo (){
        long mlseg = System.currentTimeMillis();

    }


}
