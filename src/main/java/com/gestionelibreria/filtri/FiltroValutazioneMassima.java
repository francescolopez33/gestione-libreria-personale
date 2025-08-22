package com.gestionelibreria.filtri;
import com.gestionelibreria.model.Libro;

import java.util.List;
import java.util.stream.Collectors;

public class FiltroValutazioneMassima extends FiltroDecorator{
    private final int valutazioneMassima;

    public FiltroValutazioneMassima(FiltroLibro filtroPrecedente, int valutazioneMassima) {
        super(filtroPrecedente);
        this.valutazioneMassima = valutazioneMassima;
    }//costruttore

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> libriFiltrati = super.filtra(libri);
        return libriFiltrati.stream()
                .filter(libro -> libro.getValutazione() <= valutazioneMassima)
                .collect(Collectors.toList());
    }//filtra

}//FiltroValMax
