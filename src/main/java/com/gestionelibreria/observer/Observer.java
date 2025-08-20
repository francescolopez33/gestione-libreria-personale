package com.gestionelibreria.observer;


import com.gestionelibreria.model.Libro;
import java.util.List;


//riceve lista libri aggiornata della libreria
public interface Observer {
    void update(List<Libro> libri);

}//Observer
