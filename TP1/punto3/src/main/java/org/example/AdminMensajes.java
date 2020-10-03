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
        System.out.println("entro al run");
        try {
            while (true){
                System.out.println("entro a while de admin mensajes");
                String input = in.readLine();
                Gson gson = new Gson();

                //trato de obtener el mensaje a partir del input.
                Funcion f = gson.fromJson(input,Funcion.class);
                System.out.println("antes del switch");
                if (f != null){
                    switch (f.getId()){
                        case 1:guardarMensaje(f);
                            break;  
                        case 2:
                            visualizarMensaje(f);
                            break;
                    }
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void guardarMensaje(Funcion f){
        System.out.println("entro a guardarmensaje");
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


}
