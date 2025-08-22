package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class OrdinamentoPerTitolo implements OrdinamentoStrategy{

    @Override
    public List<Libro> ordina(List<Libro> libri) {
        return libri.stream()
                .sorted(Comparator.comparing(Libro::getTitolo, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }//ordina


}//OrdinamentoXTitolo
