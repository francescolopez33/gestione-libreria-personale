package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;

import java.util.List;
import java.util.stream.Collectors;

public class RicercaContext {


    private RicercaStrategy strategy;

    public RicercaContext(RicercaStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RicercaStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Libro> ricerca(List<Libro> libri, String testo) {
        return libri.stream()
                .filter(libro -> strategy.ricercaOK(libro, testo))
                .collect(Collectors.toList());
    }
}
