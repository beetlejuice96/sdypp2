package server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class Main {
    //step[0]-> crear logger
    //step[1]-> crear conexion con rabbit.
    //step[3]-> poner a escuchar el server en un puerto.

    //variables del logger.(ya seteada)
    private final Logger log = LoggerFactory.getLogger(Main.class);
    //variables del socket.
    private String ip;
    private int port;
    private ServerSocket server;
    //variables de rabbit;
    private String userRabbit;
    private String passwordRabbit;
    private ConnectionFactory connectionFactory;
    private Connection queueConnection;
    private Channel queueChannel;
    private String ipRabbit;
    private int portRabbit;
    public static final String rabbitmq_confi = "src/main/java/resources/rabbit.properties";
    private String inputQueueName;

    public Main(String ip, int port) throws FileNotFoundException {
        this.port=port;
        this.ip=ip;
        this.inputQueueName = "inputQueue";
        configAndConecctionRabbit();
        log.info("SE ESTABLECIO CORRECTAMENTE LA CONEXION CON  RABBIT");
    }

    public void configAndConecctionRabbit() throws FileNotFoundException {
        try (InputStream in = new FileInputStream(rabbitmq_confi)) {

            Properties properties = new Properties();
            properties.load(in);
            //seteo de todo lo que tiene que ver con rabbit
            this.userRabbit = properties.getProperty("USER");
            this.passwordRabbit = properties.getProperty("PSW");
            this.ipRabbit = properties.getProperty("IP");
            this.portRabbit = Integer.parseInt(properties.getProperty("PORT"));
            this.connectionFactory = new ConnectionFactory();
            this.connectionFactory.setUsername(this.userRabbit);
            this.connectionFactory.setPassword(this.passwordRabbit);
            this.connectionFactory.setHost(this.ipRabbit);
            this.connectionFactory.setPort(this.portRabbit);
            this.queueConnection = this.connectionFactory.newConnection();
            this.queueChannel = this.queueConnection.createChannel();
            this.queueChannel.queueDeclare(this.inputQueueName, true, false, false, null);
        }catch (ConnectException e){
            System.err.println("RabbitMQ esta caido.");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void runServer(){
        try {
            ServerSocket ss = new ServerSocket(this.port);
            log.info("SERVER RUN");
            while (true){
                Socket client = ss.accept();
                log.info("CLIENTE CONECTADO DE "+ client.getInetAddress().getCanonicalHostName()+":"+client.getPort());
                ThreadServer ts = new ThreadServer(client,this.queueChannel,this.log,this.inputQueueName);
                log.info("NUEVO SERVER CREADO");
                Thread tsThread = new Thread(ts);
                tsThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) throws FileNotFoundException {

        Main sv = new Main("localhost",20000);
        sv.runServer();

    }

}
