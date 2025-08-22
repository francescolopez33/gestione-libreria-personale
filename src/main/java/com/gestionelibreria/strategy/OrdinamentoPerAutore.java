package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class OrdinamentoPerAutore implements OrdinamentoStrategy{

    @Override
    public List<Libro> ordina(List<Libro> libri) {
        return libri.stream()
                .sorted(Comparator.comparing(Libro::getAutore, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }//ordina

}//OrdinamentoXAutore

