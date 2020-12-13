package Otros;

import Worker.Worker;
import Worker.ServiceProcessMessage;
import com.google.gson.Gson;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;


public class Balanceador {
    public final Logger log = LoggerFactory.getLogger(Balanceador.class);
    public static final String WORKER_CONFIG = "src/main/java/resources/workers.xml";
    public static final String RABBIT_CONFIG = "src/main/java/resources/rabbit.properties";

    private String userRabbit;
    private String pswRabbit;
    private ConnectionFactory connectionFactory;
    private Connection queueConnection;
    private Channel queueChannel;
    public String inputQueueName = "inputQueue";
    private String outputQueueName="outputQueue";

    public String notificationQueueName = "notificationQueue";
    public String inprocessQueueName = "inProcessQueue";
    public String GlobalStateQueueName = "GlobalStateQueue";

    public String ipRabbit;
    private int portRabbit;
    private Worker workerActual;
    private int wActual;
    private ArrayList<Worker> workersActivos;
    public Gson googleJson;
    private GlobalState globalState;
    //ME FALTA LA CARGA ACTUAL
    //ME FALTA LA CARGA MAXIMA ACTUAL
    private int cargaMaxGlobal;
    private int cargarActualGlobal;
    private DocumentBuilder builder;
    private Document documentWorkers;

    private ArrayList<String> workersList;//diccionario
    private Map<String, Thread> hilos; //por si las dudas.

    //Datos para conectarme al socket
    private Socket socketServer;


    public Balanceador(){
        setConnectRabbit();
        googleJson = new Gson();
        this.globalState  = GlobalState.GLOBAL_IDLE;
        this.cargaMaxGlobal = 0;
        this.cargarActualGlobal=0;
        log.info(" CONEXION ESTABLECIDA CON RABBIT MQ");
        hilos = new HashMap<String,Thread>();
        /*levanto todos los nodos que tengo en un archivo(serian como nodos levantados por default,
        tambien los tiene el genrator de workers)
         */



        this.loadWorkersFile();
        this.purgeQueues();

    }

    private void purgeQueues() {
        try {
            this.queueChannel.queuePurge(this.inputQueueName);
            this.queueChannel.queuePurge(this.outputQueueName);
            this.queueChannel.queuePurge(this.GlobalStateQueueName);
            //todavia no puedo purgar esas otras dos ya que no existen
            //this.queueChannel.queuePurge(this.inprocessQueueName);
            //this.queueChannel.queuePurge(this.notificationQueueName);
            for (Worker w :workersActivos){
                this.queueChannel.queuePurge(w.getId());
            }
            log.info("Se purgaron todas las colas correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void setConnectRabbit() {
        try (InputStream input = new FileInputStream(RABBIT_CONFIG)){
            Properties properties = new Properties();
            properties.load(input);
            this.ipRabbit= properties.getProperty("IP");
            this.portRabbit=Integer.parseInt(properties.getProperty("PORT"));
            this.userRabbit=properties.getProperty("USER");
            this.pswRabbit= properties.getProperty("PSW");
            this.connectionFactory = new ConnectionFactory();
            this.connectionFactory.setUsername(this.userRabbit);
            this.connectionFactory.setPassword(this.pswRabbit);
            this.connectionFactory.setHost(this.ipRabbit);
            this.connectionFactory.setPort(this.portRabbit);
            this.queueConnection = this.connectionFactory.newConnection();
            this.queueChannel  = this.queueConnection.createChannel();
            //ACA DEFINE UN BASICQOS QUE NO SE QUE ES. PERO SEGURO LO NECESITE
            this.queueChannel.queueDeclare(this.inputQueueName,true,false,false,null);
            this.queueChannel.queueDeclare(this.outputQueueName,true,false,false,null);
            //tal vez necesite una cola para el estado global.
            this.queueChannel.queueDeclare(this.GlobalStateQueueName, true, false, false, null);
            //tal vez necesite una cola para las notificaciones.
            //tal vez necesite una cola para inprocess.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    public void  loadWorkersFile(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            this.builder = factory.newDocumentBuilder();
            this.documentWorkers =builder.parse(new File(WORKER_CONFIG));
            this.documentWorkers.getDocumentElement().normalize();
            NodeList elementos = documentWorkers.getElementsByTagName("worker");
            workersActivos = new ArrayList<Worker>();
            for (int i= 0; i< 3;i++) {
                Element e = (Element) elementos.item(i);
                String name = e.getAttribute("id");
                int maxload = Integer.parseInt(e.getElementsByTagName("peakload").item(0).getTextContent());
                workersActivos.add(new Worker(name,maxload));

                //suponiendo que tengo mas de un servicio
                NodeList services = e.getElementsByTagName("service");
                for (int y = 0; y<services.getLength();y++){
                    //el nombre del servicio esta harcodeado, si tuviese mas de un servicio deberia ser una lista u otra cosa.
                    String nombreJob = services.item(y).getTextContent();
                    if (services.item(y).getTextContent().equals("process")){
                        workersActivos.get(i).addService(new ServiceProcessMessage(services.item(y).getTextContent()));
                    }
                }


            }
            for(Worker w : workersActivos){
                this.increaseGlobalCurrentLoad(w.getCurrentLoad());
                this.increaseGlobalMaxLoad(w.getPeakLoad());
            }
            this.workerActual = workersActivos.get(0);
            this.wActual = -1;

            log.info("CARGA ACTUAL GLOBAL ACTUALIZADA A: " + this.cargarActualGlobal);
            log.info("CARGA MAXIMA GLOBAL ACTUALIZADA A: "+ this.cargaMaxGlobal);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //devuelve el nodo qu puede recibir la tarea.
    private Worker getNextWorker(){
        //int cantidadNodes = workersActivos.size();
        //int i = workersActivos.indexOf(workerActual);
        //return workersActivos.get(i+1);
        synchronized (this.workersActivos){

        if(this.wActual == workersActivos.size()-1){
            this.wActual=0;
        }else{
            this.wActual++;
        }
        System.out.println(this.wActual);
        }
        return workersActivos.get(this.wActual);
    }


    //suscribirse a la inputQueueName
    private DeliverCallback listenInputQueue = (consumerTag, Delivery)->{
      log.info("[ListenInputQueue]  MENSAJE RECIBIDO DE : "+ Delivery.getEnvelope().getRoutingKey());
      log.info(new String(Delivery.getBody(), StandardCharsets.UTF_8));
      //va a tener toda la logica de asignar una tarea
      Msg msg = googleJson.fromJson(new String(Delivery.getBody(),"UTF-8"),Msg.class);
      try {

          this.workerActual =  this.getNextWorker();

          msg.putParam("to-worker",this.workerActual.getId());
          log.info("[ListenInputQueue] Envia el mensaje a un worker ");

          //envio el mensaje

          String json =  googleJson.toJson(msg);
          queueChannel.basicPublish("",this.workerActual.getId(), MessageProperties.PERSISTENT_TEXT_PLAIN, json.getBytes()); //ojo con utf8
          log.info("[ListenInputQueue] Se envio el mensaje a la cola del worker "+this.workerActual.getId());

          this.workerActual.increaseCurrentLoad();//incremento la carga del worker en +1
          this.increaseGlobalCurrentLoad();//incremento la carga actual en +1
          this.updateGlobal();// actualizo el estado global pq realice cambioes en las variables de carga.
          log.info(" [+] carga actual global : "+ this.cargarActualGlobal);

      }catch (Exception e){
          log.info("[x] Service not found");
          //falta ver si ponemos esto en una cola
      }
    };

    private DeliverCallback listenOutputQueue = (consumertag, delivery)->{
        //log.info("[ListenOutput] RESPUESTA RECIBIDA DE : "+ delivery.getEnvelope().getRoutingKey());
        //log.info(new String(delivery.getBody(), StandardCharsets.UTF_8));
        try{
            this.socketServer = new Socket("localhost", 20000);
            PrintWriter out = new PrintWriter(socketServer.getOutputStream(),true);
            out.println(new String(delivery.getBody(),"UTF-8"));
            log.info("[balanceador] se envio el mensaje al cliente correctamente");
            socketServer.close();
            //en este momento tengo que hacer el descuento de tareas asignadas a un worker
            Msg msg = googleJson.fromJson(new String(delivery.getBody(),"UTF-8"),Msg.class);
            Worker w = this.findWorker(msg.parametros.get("to-worker"));
            //descuento una tarea al worker ya que se esta mandando la respuesta.
            w.decreaseCurrentLoad();
            this.decreaseGlobalCurrentLoad();

            //tambien mandar a recalcular el estado global
            this.updateGlobal();
            //luego meter toda la logica de levantar y bajar workers



        }catch (IOException e){
            e.printStackTrace();
        }

        Msg msg = googleJson.fromJson(new String(delivery.getBody(),"UTF-8"),Msg.class);


    };

    private DeliverCallback listenGlobalStatequeue=(consumertag,delivery)->{
        GlobalState gs = googleJson.fromJson(new String(delivery.getBody(),"UTF-8"),GlobalState.class);
        if (gs.equals(GlobalState.GLOBAL_IDLE)){
            System.out.println("entro al estado de global ide");
            this.dropWorker();
        }
    };

    //incrementa la carga actual con lo que se pasa por parametro
    private void increaseGlobalCurrentLoad(int currentLoad) {
        this.cargarActualGlobal += currentLoad;
    }
    private void increaseGlobalCurrentLoad() {
        this.cargarActualGlobal ++;
    }

    //incrementa la carga maxima global
    private void increaseGlobalMaxLoad(int maxLoad) {
        this.cargaMaxGlobal += maxLoad;
    }

    private void decreaseGlobalCurrentLoad(int currentLoad) {
        this.cargarActualGlobal = (this.cargarActualGlobal > 0) ? this.cargarActualGlobal - currentLoad : 0;
    }
    public void decreaseGlobalCurrentLoad() {
        this.decreaseGlobalCurrentLoad(1);
    }

    //busca un worker
    public Worker findWorker(String idWorker){
        int pos=-1;
        synchronized (this.workersActivos){
            for (int i =0; i<this.workersActivos.size();i++){
                if (this.workersActivos.get(i).getId().equals(idWorker)){
                    return workersActivos.get(i);

                }
            }
        }
        return null;
    }

    // esto va a checkear si hay un ocioso y los va a bajar
    public void dropWorker(){
        int pos = -1;
        Worker wRemove = null;
        synchronized (this.workersActivos){
            for(int i=0; i<this.workersActivos.size()-1;i++){
                //System.out.println(this.workersActivos.get(i).getCurrentLoad());
                if ((pos==-1)&&(this.workersActivos.get(i).getCurrentLoad()==0)){
                    pos=i;
                    wRemove = this.workersActivos.get(i);
                    System.out.println("entrooooooo");
                }
            }

            if ((this.wActual==pos)&&(pos!=-1)){
                this.workerActual=this.getNextWorker();
                log.info("[dropWorker] se dropeo el worker "+ wRemove.getId()+ " su posicion era: "+pos);

                if (wRemove!=null) {
                    this.workersActivos.remove(wRemove);

                }
                log.info("[dropWorker] se actualizo el tamaño de la cola, ahora es:"+this.workersActivos.size());
            }else if (pos!=-1){
                log.info("[dropWorker] se dropeo el worker "+ wRemove.getId()+ " su posicion era: "+pos);

                if (wRemove!=null) {
                    this.workersActivos.remove(wRemove);
                }
                log.info("[dropWorker] se actualizo el tamaño de la cola, ahora es:"+this.workersActivos.size());
            }else{
                log.info("[dropWorker] no se dropeo el worker porque todos tienen carga");
            }
        }


    }

    //actualizo el estado global(normalmente se usa cuando se incrementa o decrementa la carga actual)
    public void updateGlobal() throws IOException{
        // if GlobalCurrentLoad represents less than a 20% of GlobalLoad --> GlobalState is IDLE
        log.info(" [UPDATE_GLOBAL] CURR: " + this.cargarActualGlobal + " - MAX:" + this.cargaMaxGlobal);
        if (this.cargarActualGlobal  < (this.cargaMaxGlobal* 0.2)) {
            this.globalState = GlobalState.GLOBAL_IDLE;
            // if GlobalCurrentLoad represents less than a 50% of GlobalLoad --> GlobalState is NORMAL
        } else if (this.cargarActualGlobal < (this.cargaMaxGlobal * 0.5)) {
            this.globalState = GlobalState.GLOBAL_NORMAL;
            // if GlobalCurrentLoad represents less than a 80% of GlobalLoad --> GlobalState is ALERT
        } else if (this.cargarActualGlobal < (this.cargaMaxGlobal * 0.8)) {
            this.globalState = GlobalState.GLOBAL_ALERT;
            // if GlobalCurrentLoad represents more than a 80% of GlobalLoad --> GlobalState is CRITICAL
        } else {
            this.globalState = GlobalState.GLOBAL_CRITICAL;
        }
        log.info(" [UPDATE_GLOBAL] Nuevo estado global ---> " + this.globalState);
        String JsonMsg = googleJson.toJson(this.globalState);
        //if (this.queueChannel.messageCount(GlobalStateQueueName) > 0) this.queueChannel.basicGet(GlobalStateQueueName, true); no se que hace esto
        this.queueChannel.basicPublish("", GlobalStateQueueName, MessageProperties.PERSISTENT_TEXT_PLAIN, JsonMsg.getBytes("UTF-8"));
    }

    public void starBalanceador(){
        log.info(" Balanceador Started");
        try {
            this.queueChannel.basicConsume(inputQueueName, true, listenInputQueue, consumerTag -> {});
            this.queueChannel.basicConsume(outputQueueName, true, listenOutputQueue, consumerTag -> {});
            this.queueChannel.basicConsume(GlobalStateQueueName, true, listenGlobalStatequeue, consumerTag -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        int thread = (int) Thread.currentThread().getId();
        String packetName = Balanceador.class.getSimpleName()+"-"+thread;
        System.setProperty("log.name",packetName);
        Balanceador ss = new Balanceador();
        ss.starBalanceador();
    }


    






}
