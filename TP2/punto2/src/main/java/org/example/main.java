package org.example;

import java.io.IOException;
import java.util.Random;

/**
 * Hello world!
 *
 */
public class main
{
    public static void main( String[] args ) throws IOException {

        for (int i = 0; i <(new Random().nextInt(10)+5);i++){
            ClienteDeposito clienteDeposito = new ClienteDeposito(9000);
            clienteDeposito.deposito();

            ClienteExtraccion clienteExtraccion = new ClienteExtraccion(10000);
            clienteExtraccion.extraccion();
        }



    }
}
