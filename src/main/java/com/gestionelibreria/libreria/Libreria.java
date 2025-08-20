package com.gestionelibreria.libreria;

import com.gestionelibreria.model.Libro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Libreria {

    //singleton
    private static Libreria instance;

    private final List<Libro> libri;


    private Libreria() {
        this.libri = new ArrayList<>();
    }//costr


    public static synchronized Libreria getInstance() {
        if (instance == null) {
            instance = new Libreria();
        }
        return instance;
    }//getInstance



    public boolean aggiungiLibro(Libro libro) {
        //Controllo dell'ISBN
        for (Libro l : libri) {
            if (l.getIsbn().equals(libro.getIsbn())) {
                return false; //ISBN già presente in lista libri
            }
        }
        return libri.add(libro);
    }//aggiungiLibro


    public boolean rimuoviLibro(String isbn) {
        return libri.removeIf(l -> l.getIsbn().equals(isbn));
    }//rimuoviLibro

    public boolean modificaLibro(Libro libro) {
        for (int i = 0; i < libri.size(); i++) {
            if (libri.get(i).getIsbn().equals(libro.getIsbn())) {
                libri.set(i, libro);
                return true;
            }
        }
        return false;
    }//modificaLibro


    public List<Libro> getLibri() {
        return Collections.unmodifiableList(libri);
    }//getLibri



    public void svuotaLibreria() {
        libri.clear();
    }//svuota

}//libreria
