package org.example;

import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.function.Function;

public class AdminMensajes  implements  Runnable{
    private Socket cliente;
    private BufferedReader in;
    private PrintWriter out;

    private Mensajes mensajes;

    public  AdminMensajes(Socket cliente, Mensajes mensajes )throws IOException{
        this.cliente = cliente;
        this.mensajes =mensajes;

        this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

        this.out = new PrintWriter(cliente.getOutputStream(), true);

    }



    @Override
    public void run() {
        try {
            while (true){
                String input = in.readLine();
                Gson gson = new Gson();

                //trato de obtener el mensaje a partir del input.
                Funcion f = gson.fromJson(input,Funcion.class);
                if (f != null){
                    switch (f.getId()){
                        case 1:guardarMensaje(f);
                            break;  
                        case 2:
                            visualizarMensaje(f);
                            break;
                        case 3:
                            borrarMensajes(f);
                            break;
                    }
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void guardarMensaje(Funcion f){
        Gson gson = new Gson();
        Mensaje m = gson.fromJson(f.getBody(),Mensaje.class);

        this.mensajes.add(m);

        System.out.println("Mensaje almacenado.");

        out.println("Mensaje almacenado.");

    }

    private void visualizarMensaje(Funcion f){
        Gson gson  = new Gson();
        String destino = f.getBody();
        List<Mensaje> mensajesRespuesta = mensajes.getMensajePara(destino);
        String output = gson.toJson(mensajesRespuesta);
        if (mensajesRespuesta.isEmpty()){
            output = "No se encontraron mensajes para este destinatario";
        }
        //System.out.println(output);
        out.println(output);
    }

    private void borrarMensajes(Funcion f){
        Gson gson = new Gson();
        Mensaje ackBorrar  = gson.fromJson(f.getBody(),Mensaje.class);

        if (ackBorrar.getCuerpo().equals("ok")){
            mensajes.borrar(ackBorrar.getDestino());
            System.out.println("mensakes borrados");
            out.println("Mensajes de "+ackBorrar.getDestino()+" borrados");
        }
    }


}
