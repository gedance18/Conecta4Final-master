package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SrvTcp {

    private int port;
    private Tauler t;
    private int turno;
    private int numeroDeJugadores;

    private SrvTcp(int port ) {
        this.port = port;

    }

    private void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                //sumem 1 al numero de jugadors
                numeroDeJugadores++;

                if(numeroDeJugadores % 2 !=0 ){
                    turno = 1;
                    t = new Tauler();
                }else{
                    turno = 2;
                }

                t.addNUmPlayers();

                ThreadServidor FilServidor = new ThreadServidor(clientSocket, t,turno);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SrvTcp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        SrvTcp srv = new SrvTcp(5558);
        srv.listen();
    }
}