package com.gestionelibreria.filtri;

import com.gestionelibreria.model.Libro;

import java.util.List;

public class FiltroConcreto implements FiltroLibro {

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        return libri;
    }//filtra

}//FiltroConcreto
