package com.gestionelibreria.filtri;
import com.gestionelibreria.model.Libro;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroGenere extends FiltroDecorator{
    private final String genere;

    public FiltroGenere(FiltroLibro filtroPrecedente, String genere) {
        super(filtroPrecedente);
        this.genere = genere;
    }//costr

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> libriFiltrati = super.filtra(libri);
        return libriFiltrati.stream()
                .filter(libro -> libro.getGenere().equalsIgnoreCase(genere))
                .collect(Collectors.toList());
    }//filtra

}//Filtrogenere
