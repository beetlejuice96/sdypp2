package server;

import Otros.Msg;
import com.google.gson.Gson;
import com.mchange.v2.cmdline.MissingSwitchException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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
            //Msg msj = yeison.fromJson(in.readLine(),Msg.class); asi seria para leer
             // aca esta enviando el gson de una.

            //while (true){
                String json=in.readLine();
                Msg msg =yeison.fromJson(json, Msg.class);
                if (msg.getResultado().isEmpty()){
                    sendMessage(yeison.toJson(msg));
                    log.info("servidor envio mensaje a la cola");
                }else{
                    log.info(json);
                    log.info("Respuesta llego a cliente: "+msg.getResultado());
                }

            //}

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
