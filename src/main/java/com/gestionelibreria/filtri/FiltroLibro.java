package com.gestionelibreria.filtri;

import com.gestionelibreria.model.Libro;
import java.util.List;



public interface FiltroLibro {

    List<Libro> filtra(List<Libro> libri);

}//filtroLibro
