package com.gestionelibreria;

import com.gestionelibreria.view.ConsoleUI;
import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;
import com.gestionelibreria.observer.SalvataggioAutoObserver;
import com.gestionelibreria.persistenza.FileLibreriaRepository;

import java.util.List;

public class Main {



    public static void main(String[] args) {

        Libreria libreria = Libreria.getInstance();

        List<Libro> libriSalvati = FileLibreriaRepository.getInstance().carica();
        if (!libriSalvati.isEmpty()) {
            for (Libro l : libriSalvati) {
                libreria.aggiungiLibro(l);
            }
            System.out.println("Libreria caricata con " + libriSalvati.size() + " libri salvati.");
        } else {
            System.out.println("Nessun libro trovato, libreria vuota.");
        }


        libreria.registraObserver(new SalvataggioAutoObserver());


        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }//main




}//MAIN