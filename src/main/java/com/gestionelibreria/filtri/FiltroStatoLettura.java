package com.gestionelibreria.filtri;

import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroStatoLettura extends FiltroDecorator {

    private final StatoLettura stato;

    public FiltroStatoLettura(FiltroLibro filtroPrecedente, StatoLettura stato) {
        super(filtroPrecedente);
        this.stato = stato;
    }//costr

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> libriFiltrati = super.filtra(libri);
        return libriFiltrati.stream()
                .filter(libro -> libro.getStatoLettura() == stato)
                .collect(Collectors.toList());
    }//filtra

}//FiltroStatoLett
