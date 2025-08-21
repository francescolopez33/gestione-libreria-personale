package com.gestionelibreria.command;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;

public class AggiungiLibroCMD implements Comando{


    private final Libro libro;

    public AggiungiLibroCMD(Libro libro) {
        this.libro = libro;
    }

    @Override
    public void esegui() {
        Libreria.getInstance().aggiungiLibro(libro);
    }//esegui


    @Override
    public void annulla() {
        Libreria.getInstance().rimuoviLibro(libro.getIsbn());
    }//annulla



    public Libro getLibro() {
        return libro;
    }//getLibro

}//aggiungiLibroCMD
