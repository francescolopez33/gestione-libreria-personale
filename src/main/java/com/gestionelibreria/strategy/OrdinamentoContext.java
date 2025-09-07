package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;

import java.util.List;

public class OrdinamentoContext {

    private OrdinamentoStrategy strategy;

    public OrdinamentoContext(OrdinamentoStrategy strategy) {
        this.strategy = strategy;

    }//costr

    public void setStrategy(OrdinamentoStrategy strategy) {
        this.strategy = strategy;
    }//setStrategy

    public List<Libro> ordina(List<Libro> libri) {
        return strategy.ordina(libri);
    }//ordina


}//ORDINAMENTOCONTEXT
