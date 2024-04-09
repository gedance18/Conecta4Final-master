package com.company;

import java.io.*;
import java.net.Socket;

public class ThreadServidor implements Runnable {

    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private Tauler tauler;
    private boolean acabat;
    private boolean ganar = false;
    private int puntos = 0, fila = 5;
    private int puntosD = 0;


    public ThreadServidor(Socket clientSocket, Tauler t, int turno) throws IOException {
        this.clientSocket = clientSocket;
        tauler = t;
        //Al inici de la comunicació el resultat ha de ser diferent de 0(encertat)
        tauler.resultat = 3;
        acabat = false;
        //Enllacem els canals de comunicació
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        System.out.println("canals i/o creats amb un nou jugador");
    }

    @Override
    public void run() {
        Jugada j = null;
        try {
            while(!acabat) {

                //Enviem tauler al jugador
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(tauler);
                oos.flush();

                //Llegim la jugada
                ObjectInputStream ois = new ObjectInputStream(in);
                try {
                    j = (Jugada) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("jugada: " + j.Nom + "->" + j.num);
                if(!tauler.map_jugadors.containsKey(j.Nom)){
                    tauler.map_jugadors.put(j.Nom, Integer.parseInt(j.numeroDeJugador));
                }
                else {
                    //Si el judador ja esxiteix, actualitzem la quatitat de tirades
                    int tirades = tauler.map_jugadors.get(j.Nom) + 1;

                    int a = Integer.parseInt(j.numeroDeJugador);
                    boolean columnainvalida = false;
                    if (j.num > 0 && j.num < 8){
                        columnainvalida = true;
                    }
                    if (tauler.map_jugadors.get(j.Nom) == tauler.turno && columnainvalida) {

                        for (int i = 5; i > -1; i--) {
                            if (tauler.matrix[i][j.num-1] == 0){
                                tauler.matrix[i][j.num-1] = Integer.parseInt(j.numeroDeJugador);
                                break;
                            }else{
                                fila--;
                            }
                        }


                        for (int i = 0; i < tauler.matrix.length - 1; i++) {
                            if (tauler.matrix[i][j.num-1] == Integer.parseInt(j.numeroDeJugador) && tauler.matrix[i+1][j.num-1] == Integer.parseInt(j.numeroDeJugador)){
                                puntos++;
                                if (puntos == 3){
                                    System.out.println("Vertical");
                                    break;
                                }
                            }else{
                                puntos = 0;}

                        }

                        if (puntos != 3){
                            for (int i = 0; i < 5; i++) {
                                if (tauler.matrix[fila][i] == Integer.parseInt(j.numeroDeJugador) && tauler.matrix[fila][i+1] == Integer.parseInt(j.numeroDeJugador)){
                                    puntos++;
                                    if (puntos == 3){
                                        System.out.println("Horizontal");
                                        break;
                                    }
                                }else{
                                    puntos = 0;}
                            }
                            fila = 5; //ERROR
                        }


                        if (puntos != 3){

                            for (int k = 5; k > -1; k--) {
                                int y = 0;
                                int x = 0;
                                for (int i = k; i > -1; i--) {
                                    if (tauler.matrix[i][x] == Integer.parseInt(j.numeroDeJugador)){
                                        System.out.println("D1["+ i + "] [" + x + "]" + " " +Integer.parseInt(j.numeroDeJugador));
                                        puntosD++;
                                        if (puntosD == 4){
                                            System.out.println("Diagonal1");
                                            break;}
                                    }else {
                                        puntosD = 0;
                                    }
                                    x++;
                                }
                                if (puntosD == 4){break;}else {
                                    puntosD = 0;
                                }
                            }

                        }

                        if (puntosD != 4){
                            int z = 0;
                            for (int k = 5; k > -1; k--) {
                                int y = 5;
                                int x = 1 +z;

                                for (int i = k; i > -1; i--) {
                                    if (tauler.matrix[i][x] == Integer.parseInt(j.numeroDeJugador)){
                                        System.out.println("D2["+ i + "] [" + x + "]" + " " +Integer.parseInt(j.numeroDeJugador));
                                        puntosD++;
                                        if (puntosD == 4){
                                            System.out.println("Diagonal2");
                                            break;}
                                    }else {
                                        puntosD = 0;
                                    }
                                    x++;
                                }
                                z++;
                                if (puntosD == 4){break;}else {
                                    puntosD = 0;
                                }
                            }

                        }

                        if (puntosD != 4){
                            int y = 0;
                            for (int k = 5; k > -1; k--) {
                                int x = 6;
                                for (int i = k; i > -1; i--) {
                                    if (tauler.matrix[i][x] == Integer.parseInt(j.numeroDeJugador)){
                                        System.out.println("D3["+ i + "] [" + x + "]" + " " +Integer.parseInt(j.numeroDeJugador));
                                        puntosD++;
                                        if (puntosD == 4){
                                            System.out.println("Diagonal3");
                                            break;}
                                    }else {
                                        puntosD = 0;
                                    }
                                    x--;
                                }
                                if (puntosD == 4){break;}else {
                                    puntosD = 0;
                                }

                            }

                        }


                        if (puntosD != 4){
                            int z = 0;
                            for (int k = 5; k > -1; k--) {
                                int y = 5;
                                int x =k -z;

                                for (int i = k; i > -1; i--) {
                                    if (tauler.matrix[y][x] == Integer.parseInt(j.numeroDeJugador)){
                                        System.out.println("D4["+ y + "] [" + x + "]" + " " +Integer.parseInt(j.numeroDeJugador));
                                        puntosD++;
                                        if (puntosD == 4){
                                            System.out.println("Diagonal4");
                                            break;}
                                    }else {
                                        puntosD = 0;
                                    }
                                    x--;
                                    y--;
                                }
                                //z++;
                                if (puntosD == 4){break;}else {
                                    puntosD = 0;
                                }

                            }

                        }

                        if (puntos == 3 || puntosD == 4){
                            ganar = true;
                            System.out.println("Has ganado!");
                            tauler.finalTurno();
                        }else{tauler.cambioTurno();}
                    }
                    Thread.sleep(4000);
                    tauler.map_jugadors.put(j.Nom, a);

                }

                //comprobar la jugada i actualitzar tauler amb el resultat de la jugada
                //tauler.resultat = ns.comprova(j.num);
                tauler.resultat = 4;
                if(ganar) {
                    acabat = true;
                    System.out.println(j.Nom + " A Guanyat!");
                    tauler.acabats++;
                    tauler.resultat = 0;
                }
            }
        }catch(IOException | InterruptedException e){
            System.out.println(e.getLocalizedMessage());
        }
        //Enviem últim estat del tauler abans de acabar amb la comunicació i acabem
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(tauler);
            oos.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
