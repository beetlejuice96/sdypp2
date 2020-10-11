package org.example;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    private static Scanner reader;
    private static BufferedReader in;
    private static PrintWriter out;


    public static void main(String[] args){

        try {
            Socket s = new Socket("127.0.0.1",9000);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(),true);

            reader = new Scanner(System.in);

            System.out.println(in.readLine());
            out.println("Hola soy el cliente");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
