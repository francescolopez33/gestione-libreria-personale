package com.gestionelibreria.filtri;

import com.gestionelibreria.model.Libro;

import java.util.List;

public class FiltroDecorator implements FiltroLibro{

    protected FiltroLibro filtroPrecedente;

    public FiltroDecorator(FiltroLibro filtroPrecedente) {
        this.filtroPrecedente = filtroPrecedente;
    }//costr

    @Override
    public List<Libro> filtra(List<Libro> libri) {

        return filtroPrecedente.filtra(libri);
    }//filtra


}//FiltroDecorator
