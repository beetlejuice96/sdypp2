package server;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServer implements Runnable {
    private Socket cliente;
    private Channel channel;//canal de la conexion a rabbit que se le pasa desde Main;
    private Gson yeison; //JJAJJAJAJA
    private Logger log;
    private String inputQueueName;


    public ThreadServer(Socket cliente , Channel channel, Logger log, String inputQueueName){
        this.cliente=cliente;
        this.channel = channel;
        this.log = log;
        this.inputQueueName = inputQueueName;
        this.yeison = new Gson();
    }

    public void sendMessage(String mensaje) throws IOException {
        this.channel.basicPublish("",this.inputQueueName, MessageProperties.PERSISTENT_TEXT_PLAIN,mensaje.getBytes());
        this.log.info("EL CLIENTE PROCEDE A ENVIAR UN MENSAJE A LA COLA.");
    }

    @Override
    public void run() {
        try{

            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter(cliente.getOutputStream(),true);
            sendMessage(in.readLine());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
