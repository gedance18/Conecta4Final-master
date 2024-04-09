package com.company;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTcp extends Thread {

    private String Nom;
    private String NumeroDeJugador;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Scanner scin;
    private boolean continueConnected;
    private Tauler t;
    private Jugada j;
    private String OtroString = "null";
    private int OtroInt = 0;
    private boolean puerta = false;


    private ClientTcp(String hostname, int port) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        continueConnected = true;
        scin = new Scanner(System.in);
        j = new Jugada();
    }

    public void run() {
        String msg = null;
        while(continueConnected) {
            //Llegir info del servidor (estat del tauler)
            t = getRequest();

            //Crear codi de resposta a missatge
            switch (t.resultat) {
                case 4:
                    msg = "Espera un momento " + Nom + " - ";
                    break;
                case 3:
                    msg = "Benvingut al joc " + Nom + " - ";
                    break;
                case 2:
                    msg = "Més gran";
                    break;
                case 1:
                    msg = "Més petit";
                    break;
                case 0:
                    System.out.println("Conecta 4!");
                    System.out.println(t);
                    continueConnected = false;
                    continue;
            }
            System.out.println(msg);
            System.out.println(t);
            for (int i = 0; i < t.matrix.length; i++) {
                for (int k = 0; k < t.matrix[i].length; k++) {
                    System.out.print(t.matrix[i][k] + " ");
                }
                System.out.println();
            }

            try {

                if (t.turno == 3){
                    System.out.println("Has pedido :(");
                    System.out.println(t);
                    continueConnected = false;
                    continue;
                }
                if (t.map_jugadors.get(j.Nom) == t.turno) {
                    if (t.resultat != 0) {
                        System.out.println("Entra un número: ");
                        /*for (int i = 0; i < t.matrix.length; i++) {
                            for (int k = 0; k < t.matrix[i].length; k++) {
                                System.out.print(t.matrix[i][k] + " ");
                            }
                            System.out.println();
                        }*/
                        j.num = scin.nextInt();
                        /*j.Nom = Nom;
                        j.numeroDeJugador = NumeroDeJugador;
                        */
                        t.cambioTurno();
                    }


                }

            }catch (Exception e){

            }


            try {
                j.numeroDeJugador = NumeroDeJugador;
                j.Nom = Nom;

                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(j);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        close(socket);

    }
    private Tauler getRequest() {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            t = (Tauler) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }


    private void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(ClientTcp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        String jugador, ipSrv;
        String numeroDeJugador;

        //Demanem la ip del servidor i nom del jugador
        System.out.println("Ip del servidor?");
        Scanner sip = new Scanner(System.in);
        ipSrv = sip.next();
        System.out.println("Nom jugador:");
        jugador = sip.next();
        System.out.println("Numero de jugador");
        numeroDeJugador = sip.next();

        ClientTcp clientTcp = new ClientTcp(ipSrv,5558);
        clientTcp.Nom = jugador;
        clientTcp.NumeroDeJugador = numeroDeJugador;
        clientTcp.start();
    }
}
