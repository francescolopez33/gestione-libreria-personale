package com.gestionelibreria.strategy;

import com.gestionelibreria.model.Libro;
import java.util.List;


public interface OrdinamentoStrategy {

    List<Libro> ordina(List<Libro> libri);

}//OrdinamentoStrategy