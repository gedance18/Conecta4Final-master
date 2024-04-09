package com.company;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Tauler implements Serializable {
    public Map<String,Integer> map_jugadors;
    public int resultat = 3, acabats;
    public int turno = 1;
    private int numPlayers;
    public int matrix[][] = new int[6][7];

    public Tauler() {
        map_jugadors = new HashMap<>();
        acabats = 0;
    }

    public void addNUmPlayers() {
        this.numPlayers++;
    }

    public int cambioTurno() {
        if(turno == 1) {
            turno = 2;
        }
        else {
            turno = 1;
        }
        return turno;
    }

    public int finalTurno(){
        turno = 3;
        return turno;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //sb.append("Intents\n");
        map_jugadors.forEach((k,v) -> sb.append(k + " - " + v + "\n"));
        return sb.toString();
    }
}

class Jugada implements Serializable {
    String Nom;
    int num;
    String numeroDeJugador;
}
