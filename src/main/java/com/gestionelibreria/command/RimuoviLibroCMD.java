package com.gestionelibreria.command;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;

public class RimuoviLibroCMD implements Comando{


    private final String isbn;
    private Libro libroRimosso;

    public RimuoviLibroCMD(Libreria libreria, String isbn) {
        this.libroRimosso = null;
        this.isbn = isbn;
    }

    @Override
    public void esegui() {

        for (Libro l : Libreria.getInstance().getLibri()) {
            if (l.getIsbn().equals(isbn)) {
                libroRimosso = l;
                break;
            }
        }
        Libreria.getInstance().rimuoviLibro(isbn);

    }//esegui

    @Override
    public void annulla() {
        if (libroRimosso != null) {
            Libreria.getInstance().aggiungiLibro(libroRimosso);
        }
    }//annulla

    public String getIsbn() {
        return isbn;
    }//getIsbn

}//RimuoviLibro
