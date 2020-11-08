package org.example;

import java.io.*;
import java.net.Socket;

public class ExtraccionC implements Runnable {
    private ServidorExtraccion server;
    private String cuenta;
    private Socket cliente;
    private BufferedReader reader;
    private PrintWriter printer;
    private BufferedWriter writer;



    public ExtraccionC(String cuenta, Socket cliente, ServidorExtraccion serverE){ //el servidor solo lo paso par utilizacion del logger
        this.cliente = cliente;
        this.cuenta = cuenta;
        this.server = serverE;

        try {
            reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            printer = new PrintWriter(cliente.getOutputStream(),true);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    //dentro se realiza la extraccion del saldo en el caso que no se pueda se retorna un mensaje al cliente que posee saldo insufuciente
    @Override
    public void run() {
        printer.println("Ingrese el monto a retirar");
        try {
            synchronized (cuenta) {
                String in = reader.readLine();
                Double saldoActual = getSaldo();
                Double montoExtraccion = Double.parseDouble(in);


                if (saldoActual < montoExtraccion) {
                    printer.println("El saldo es insuficiente .\n Saldo actual=>" + saldoActual);
                } else {
                    extraer(montoExtraccion);
                    Double saldoActualizado = getSaldo();
                    printer.println("Se realizo la extraccion con exito. \n Saldo actual =>" + saldoActualizado);
                }

                //logeo la transaccion que se realizo o se quizo realizar.
                server.logeo("Saldo anterior:" + saldoActual + " | Saldo a extraer: " + montoExtraccion + " | Nuevo saldo :" + getSaldo());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    //consulta de saldo
    public double getSaldo(){
        try {
            BufferedReader freader = new BufferedReader(new FileReader(cuenta));
            String valor = freader.readLine();
            freader.close();
            System.out.println(valor);
            Double saldo = Double.parseDouble(valor);
            return saldo;
        }catch (IOException e){

        }

        return 0;
    }



    //si entra a extraer es por que ya hizo la validacion de si puede hacerlo.
    public void extraer(Double montoExtraccion){
        double saldoActual = getSaldo();
        double nuevoSaldo = saldoActual-montoExtraccion;
        try {
            Thread.sleep(80); //serian lo 80 que se tardan en extraer.
            writer = new BufferedWriter(new FileWriter(cuenta));
            writer.write(Double.toString(nuevoSaldo));
            writer.flush();
            //no hago el close.
            writer.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
