package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class OrdinamentoPerValutazione implements OrdinamentoStrategy{

    @Override
    public List<Libro> ordina(List<Libro> libri) {
        return libri.stream()
                .sorted(Comparator.comparingInt(Libro::getValutazione).reversed()) //dal + alto al + basso
                .collect(Collectors.toList());
    }//ordina


}//OrdinamentoXValutazione

