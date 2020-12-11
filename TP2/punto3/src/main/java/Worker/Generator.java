package Worker;

import Otros.Msg;
import com.google.gson.Gson;
import com.rabbitmq.client.*;
import org.fusesource.mqtt.codec.PUBLISH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Generator {
public final Logger log = LoggerFactory.getLogger(Generator.class);
private String userRabbit;
private String pswRabbit;
private String ipRabbit;
private int portRabbit;

private ConnectionFactory connectionFactory;
private Connection queueConnection;
private Channel queueChannel;
public String myNodeQueueName;
public static final String RABBIT_CONFIG_PROPERTIES= "src/main/java/resources/rabbit.properties";
public Gson gson;


private  static final ArrayList<Integer> workers_id = new ArrayList<Integer>(Arrays.asList(
        1,2,3
));

    public void ConnectRabbit(){
        try (InputStream input = new FileInputStream(RABBIT_CONFIG_PROPERTIES)){
            Properties properties = new Properties();
            properties.load(input);
            this.ipRabbit = properties.getProperty("IP");
            this.portRabbit = Integer.parseInt(properties.getProperty("PORT"));
            this.userRabbit = properties.getProperty("USER");
            this.pswRabbit = properties.getProperty("PSW");
            this.connectionFactory = new ConnectionFactory();
            this.connectionFactory.setHost(this.ipRabbit);
            this.connectionFactory.setPort(this.portRabbit);
            this.connectionFactory.setUsername(this.userRabbit);
            this.connectionFactory.setPassword(this.pswRabbit);
            this.queueConnection= this.connectionFactory.newConnection();
            this.queueChannel =this.queueConnection.createChannel();
            log.info("EL GENERADOR DE NODOS SE CONECTO A RABBIT CORRECTAMENTE");
            //PROXIMAMENTE PONER UNA COLA DONDE ESTE GENERADOR ESTE ESCUCHANDO PARA CREAR UN NUEVO WORKER

        }catch (IOException | TimeoutException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Generator generator = new Generator();
        generator.createWorkers();
    }

    //crea los workers que tenemos cargados en una lista(para prueba)
    public void createWorkers() throws IOException {
        ConnectRabbit();
        for (Integer worker:workers_id){
            Worker worker1 = new Worker(String.valueOf(worker),5);
            //agrego el servicio que va a poder ejecutar
            worker1.addService(new ServiceProcessMessage("process"));
            runWorker(worker1);
        }
    }

    public void runWorker(Worker worker) throws IOException {
        //inicio la cola del  worker especifico
        gson = new Gson();
        this.queueChannel.queueDeclare(worker.getId(), true, false, false, null);
        log.info(worker.getId()+ "RUN");
        DeliverCallback deliverCallback= (consumeTag,delivery)->{
            //System.out.println(Arrays.toString(delivery.getBody()));
            Msg message= gson.fromJson(new String(delivery.getBody(),"UTF-8"),Msg.class);
            log.info(delivery.getEnvelope().getRoutingKey()+ " mensaje recibido "+ gson.toJson(message));
            //Asigna Tarea a Thread
            log.info(worker.getId()+" Trabajando...");

            Random r = new Random();
            //llamar a threadWorker para que procese el mensaje.
            ThreadWorker tw = new ThreadWorker(r.nextLong(),worker,message,queueChannel,this.log);
            Thread th = new Thread(tw);
            th.start();
        };
        //suscribo al worker.
        queueChannel.basicConsume(worker.getId(), true, deliverCallback, consumerTag -> {});
    }




}
