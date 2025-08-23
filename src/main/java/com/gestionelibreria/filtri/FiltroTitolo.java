package com.gestionelibreria.filtri;

import com.gestionelibreria.model.Libro;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroTitolo extends FiltroDecorator {

    private final String titolo;

    public FiltroTitolo(FiltroLibro filtroPrecedente, String titolo) {
        super(filtroPrecedente);
        this.titolo = titolo;
    }//costr

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        // Applica prima il filtro precedente e poi aggiunge il proprio filtro per titolo.
        List<Libro> libriFiltrati = super.filtra(libri);
        return libriFiltrati.stream()
                .filter(libro -> libro.getTitolo().equalsIgnoreCase(titolo))
                .collect(Collectors.toList());
    }//filtra

}//filtroTitolo
