package com.gestionelibreria.observer;

import com.gestionelibreria.model.Libro;
import java.util.List;

public class ListaLibriView implements Observer {

    @Override
    public void update(List<Libro> libri) {
        System.out.println("La libreria è stata aggiornata. Libri attuali:");
        for (Libro libro : libri) {
            System.out.println("- " + libro.getTitolo() + " di " + libro.getAutore());
        }
    }//update


}//ListaLibriView
