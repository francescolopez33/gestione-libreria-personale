package com.gestionelibreria.command;

import java.util.ArrayList;
import java.util.List;


public class GestoreComandi {   //invoker

    private static List<Comando> storico = new ArrayList<>();

    public GestoreComandi() {}

    //Esegue comando e lo salva
    public static void eseguiComando(Comando comando) {
        comando.esegui();
        storico.add(comando);
    }//eseguiComando



    //Annulla ultimo comando
    public static void annullaUltimoComando() {
        if (!storico.isEmpty()) {
            Comando ultimo = storico.remove(storico.size() - 1);
            ultimo.annulla();
        } else {
            System.out.println("Nessun comando da annullare.");
        }
    }//annullaUltimo



    public int getStoricoSize() {
        return storico.size();
    }//getStoricoSize


}//gestoreComandi
