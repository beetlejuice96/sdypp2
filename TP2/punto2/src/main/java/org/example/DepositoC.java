package org.example;

import java.io.*;
import java.net.Socket;

public class DepositoC implements Runnable {
    private ServidorDeposito server;
    private String cuenta;
    private Socket cliente;
    private BufferedReader reader;
    private PrintWriter printer;
    private BufferedWriter writer;
    private FileReader fr;


    public DepositoC(String cuenta, Socket cliente, ServidorDeposito serverD)  {
     this.server = serverD;
     this.cliente = cliente;
     this.cuenta = cuenta;

     try {
     reader = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
     printer = new PrintWriter(cliente.getOutputStream(),true);
     //writer= new BufferedWriter(new FileWriter(cuenta));
     } catch (IOException e) {
         e.printStackTrace();
     }
    }


    @Override
    public void run() {

       printer.println("Ingrese la cantidad a depositar");

        try {
            synchronized (cuenta) {
                String in = reader.readLine();
                Double saldoActual = getSaldo();
                Double montoDeposito = Double.parseDouble(in);
                depositar(montoDeposito);

                printer.println("Se realizo el deposito correctamente \n Saldo actual => " + getSaldo());

                server.logeo("Saldo anterior:" + saldoActual + " | Saldo a depositar: " + montoDeposito + " | Nuevo saldo :" + getSaldo());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void depositar(Double montoDepositar){
        double saldoActual = getSaldo();
        double nuevoSaldo = montoDepositar+saldoActual;
        try {
            Thread.sleep(40);
            writer= new BufferedWriter(new FileWriter(cuenta));
            writer.write(Double.toString(nuevoSaldo));
            writer.flush();
            writer.close();

        }catch (InterruptedException | IOException e){
            e.printStackTrace();
        }

    }

    public double getSaldo(){
        try {
            BufferedReader freader = new BufferedReader(new FileReader(cuenta));
            String valor = freader.readLine();
            freader.close();
            Double saldo = Double.parseDouble(valor);
            return saldo;
        }catch (IOException e){

        }

        return 0;
    }

}
