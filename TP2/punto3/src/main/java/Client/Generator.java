package Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Generator {
    private final static Logger log = LoggerFactory.getLogger(Generator.class);

    public static void main(String[] args) throws IOException {
        int contador = 10;
        for (int i = 0;i<contador;i++){
            Client cliente;
            cliente= new Client("localhost",20000,log);
            Thread hilo = new Thread(cliente);
            hilo.start();
        }



    }
}
