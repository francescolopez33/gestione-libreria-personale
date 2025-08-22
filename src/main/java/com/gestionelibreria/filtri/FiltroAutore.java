package com.gestionelibreria.filtri;
import com.gestionelibreria.model.Libro;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroAutore extends FiltroDecorator{
    private final String autore;


    public FiltroAutore(FiltroLibro filtroPrecedente, String autore) {
        super(filtroPrecedente);
        this.autore = autore;
    }//costr


    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> libriFiltrati = super.filtra(libri);
        return libriFiltrati.stream()
                .filter(libro -> libro.getAutore().equalsIgnoreCase(autore))
                .collect(Collectors.toList());
    }//filtra


}//FiltroAutore
