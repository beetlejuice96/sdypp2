package Worker;

import Otros.Msg;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;

import java.net.Socket;


public class ThreadWorker  implements Runnable{
    private Channel queueChannel;
    private String activeQueueName;
    private String outputQueueName;
    private String routingKey;
    private Gson googlegson;
    private Logger log;
    private Msg tarea;
    private Worker worker;
    private  Long id;
    private Socket socketServer;

    private static final String EXCHANGE_OUTPUT = "XCHNG-OUT";


    public  ThreadWorker (Long id,Worker worker, Long routingkey, Msg msg, Channel queueChannel,String activeQueueName, String outputQueueName,Logger log){
        this.id = id;
        this.worker=worker;
        this.routingKey=String.valueOf(routingkey);
        this.queueChannel=queueChannel;
        this.activeQueueName = activeQueueName;
        this.outputQueueName = outputQueueName;
        this.tarea = msg;
        this.googlegson =new Gson();
        this.log = log;

        //aca podria ir la coneccion con el server

    }


    @Override
    public void run() {

        Service service  =  this.worker.findService(tarea.getFunctionName());

        if (service != null){
            log.info("TASK ->"+service.getName() );
            log.info(this.worker.getId()+ "  Thread - " + this.id+ ":"+ this.tarea.parametros.values());
            this.tarea.setResultado((String) service.execute(tarea.parametros.values().toArray()));//tal vez tenga que cambiarlo por un string
            log.info(this.worker.getId()+ "  Thread - " + this.id+ ": RESULTADO TAREA "+ this.tarea.getResultado());
            Msg respuesta = this.tarea;
            //en un futuro este mensaje publicarlo en una cola de respuesta y manejarlo por ahi

            System.out.println(respuesta.parametros+ "esta es la respuesta");



        }
    }
}
