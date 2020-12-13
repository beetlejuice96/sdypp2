package Worker;

import Otros.Msg;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;


public class ThreadWorker  implements Runnable{
    private Channel queueChannel;
    private String activeQueueName;
    private String outputQueueName="outputQueue";
    private String routingKey;
    private Gson googlegson;
    private Logger log;
    private Msg tarea;
    private Worker worker;
    private  Long id;
    private Socket socketServer;

    private static final String EXCHANGE_OUTPUT = "XCHNG-OUT";



    public  ThreadWorker (Long id,Worker worker, Msg msg, Channel queueChannel,Logger log){
        this.id = id;
        this.worker=worker;
        //this.routingKey=String.valueOf(routingkey); no se para que se usa por ahora
        this.queueChannel=queueChannel;
        //this.activeQueueName = activeQueueName;no se para que se usa por ahora
        //this.outputQueueName = outputQueueName;no se para que se usa por ahora
        this.tarea = msg;
        this.googlegson =new Gson();
        this.log = log;

        //aca podria ir la coneccion con el server

    }


    @Override
    public void run() {

        Service service  =  this.worker.findService(tarea.getFunctionName());

        if (service != null){
            try {
            log.info("TASK ->"+service.getName() );
            log.info(this.worker.getId()+ "  Thread - " + this.id+ ":"+ this.tarea.parametros.values());
            this.tarea.setResultado((String) service.execute(tarea.parametros.values().toArray()));//tal vez tenga que cambiarlo por un string
            log.info(this.worker.getId()+ "  Thread - " + this.id+ ": RESULTADO TAREA "+ this.tarea.getResultado());
            Msg respuesta = this.tarea;
            String mensaje = googlegson.toJson(respuesta);

            //en un futuro este mensaje publicarlo en una cola de respuesta y manejarlo por ahi

            this.queueChannel.basicPublish("",this.outputQueueName, MessageProperties.PERSISTENT_TEXT_PLAIN,mensaje.getBytes());
            System.out.println(respuesta.parametros+ "esta es la respuesta");
            } catch (IOException e) {
                e.printStackTrace();
                log.info("[x] No se pudo procesar el mensaje.");
            }

        }
    }
}
