package Client;

import Otros.Msg;
import com.google.gson.Gson;
import org.apache.logging.log4j.message.Message;
import org.slf4j.Logger;


import java.io.*;
import java.net.Socket;

public class Client implements Runnable {
    Socket socket;
    private int idCliente;
    private  final  Logger log;


public Client(String ip, int port, Logger log) throws IOException {
    this.log=log;
    socket = new Socket(ip,port);
    this.idCliente= (int) (Math.random()* 100)+1;
}


    @Override
    public void run() {
        log.info("EL CLIENTE :"+idCliente+" ESTA CORRIENDO");
        try {
            //dejamos de manejar strings y nos manejamos con objetos para poder mandar Message
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            //testeo mandar un mensaje al servidor y que el servidor envie mensaje a la cola.
            //String msj = "El CLIENTE:"+ idCliente+" ENVIA ESTE MENSAJE";
            Gson gson = new Gson();
            Msg msg = new Msg("sumar");// sumar seria un ejemplo nada mas
            msg.putParam("Cliente",String.valueOf(idCliente));
            msg.putParam("Nombre","Marquitos");
            String msj = gson.toJson(msg);
            System.out.println(msj);
            out.println(msj);

            /*ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());*/

            log.info("EL CLIENTE: "+ idCliente+" ENVIO MENSAJE AL SERVIDOR");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
