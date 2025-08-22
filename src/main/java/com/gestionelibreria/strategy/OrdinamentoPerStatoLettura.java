package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class OrdinamentoPerStatoLettura implements OrdinamentoStrategy{

    public List<Libro> ordina(List<Libro> libri) {
        // Un Comparator personalizzato per definire l'ordine degli stati
        Comparator<Libro> statoComparator = (libro1, libro2) -> {
            int stato1 = getOrdineStato(libro1.getStatoLettura());
            int stato2 = getOrdineStato(libro2.getStatoLettura());
            return Integer.compare(stato1, stato2);
        };

        return libri.stream()
                .sorted(statoComparator)
                .collect(Collectors.toList());
    }//ordina



    private int getOrdineStato(StatoLettura stato) {
        switch (stato) {
            case LETTO:
                return 1;
            case IN_LETTURA:
                return 2;
            case DA_LEGGERE:
                return 3;
            default:
                return 4; // per stati non riconosciuti
        }
    }//getOrdineStato

}//OrdinamentoXStatoLettura

