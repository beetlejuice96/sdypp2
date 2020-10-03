package delCliente;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Funcion;
import org.example.Mensaje;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client  {
        private static Scanner reader;
        private static  BufferedReader in;
        private static PrintWriter out;
    public static void main( String[] arg) {

        try{
            Socket s = new Socket("127.0.0.1",18000);

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(),true);
            reader = new Scanner(System.in);
            int op = 1;
            while (op!=0){
                op=menuPrincipal();
                switch (op){
                    case 1:nuevoMensaje();
                            System.out.println(in.readLine());
                        break;
                    case 2:visualizarMensajes();
                            listarMensajes(in.readLine());
                        break;
                    default:
                        break;
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }




    }

    private static void nuevoMensaje (){
        Mensaje nuevo = new Mensaje();
        nuevo.setOrigen(JOptionPane.showInputDialog(null,"Origen: "));
        nuevo.setDestino(JOptionPane.showInputDialog(null,"Destino: "));
        nuevo.setCuerpo(JOptionPane.showInputDialog(null,"Mensaje: "));
        System.out.println(nuevo.toString());
        Gson gson = new Gson();

        String msj = gson.toJson(nuevo);

        Funcion f = new Funcion();
        f.setId(1);
        f.setBody(msj);

        String output = gson.toJson(f);
        out.println(output);
    }

    private static void visualizarMensajes(){
        String destino = JOptionPane.showInputDialog(null,"Ingrese el nombre del destinatario:");
        Funcion f = new Funcion();
        f.setId(2);
        f.setBody(destino);
        Gson gson = new Gson();

        String msj = gson.toJson(f);
        out.println(msj);
    }


    private static int menuPrincipal(){
        String menu = "";
        menu+=("[1] nuevo mensaje \n");
        menu+=("[2] Visualizar mensajes \n");
        menu +=("[0] cerrar conexion \n");

        System.out.print(menu);
        int opcion= reader.nextInt();
        return opcion;
    }

    private static void listarMensajes(String entrada){
        Gson gson = new Gson();

        List<Mensaje> msjs = gson.fromJson(entrada, new TypeToken<List<Mensaje>>(){}.getType());

        String msg = "";
        for (Mensaje m : msjs){
            msg += m.toString()+"\n";
        }

        if (msjs.isEmpty()){
            JOptionPane.showMessageDialog(null,"No hay mensajes.");

        }else{
            JOptionPane.showMessageDialog(null,msg,"mensaje recibido",JOptionPane.PLAIN_MESSAGE);
        }

    }



}

