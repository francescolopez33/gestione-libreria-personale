package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;

public interface RicercaStrategy {

    boolean ricercaOK(Libro l, String testo);

}//RicercaStrategy
