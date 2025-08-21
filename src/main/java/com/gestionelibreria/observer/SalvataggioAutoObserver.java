package com.gestionelibreria.observer;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;
import com.gestionelibreria.persistenza.FileLibreriaRepository;

import java.util.List;

public class SalvataggioAutoObserver implements Observer {

    @Override
    public void update(List<Libro> libri) {

        //quando la librera cambia, salviamo i libri
        FileLibreriaRepository.getInstance().salva(libri);
        System.out.println("Salvataggio automatico completato.");

    }//update


}//SalvataggioAutoObserver
