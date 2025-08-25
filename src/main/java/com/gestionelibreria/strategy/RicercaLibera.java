package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;

public class RicercaLibera implements RicercaStrategy{


    @Override
    public boolean ricercaOK(Libro l, String testo) {
        String lowerCaseText = testo.toLowerCase();
        return l.getTitolo().toLowerCase().contains(lowerCaseText) ||
                l.getAutore().toLowerCase().contains(lowerCaseText) ||
                l.getGenere().toLowerCase().contains(lowerCaseText)
                || l.getIsbn().toLowerCase().contains(lowerCaseText);
    }//ok

}//ricercaLibera
