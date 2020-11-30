package Worker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.codehaus.groovy.tools.shell.IO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class Generator {
private String userRabbit;
private String pswRabbit;
private String ipRabbit;
private int portRabbit;

private ConnectionFactory connectionFactory;
private Connection queueConnection;
private Channel queueChannel;
private String myNodeQueueName;
public static final String RABBIT_CONFIG_PROPERTIES= "src/main/java/resources/rabbit.properties";


private  static final ArrayList<Integer> workers_id = new ArrayList<Integer>(Arrays.asList(
        1,2
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
        //PROXIMAMENTE PONER UNA COLA DONDE ESTE GENERADOR ESTE ESCUCHANDO PARA CREAR UN NUEVO WORKER

    }catch (IOException | TimeoutException e){

    }
}




}
