package com.gestionelibreria.filtri;
import com.gestionelibreria.model.Libro;
import java.util.List;
import java.util.stream.Collectors;


public class FiltroValutazioneMinima extends FiltroDecorator{
    private final int valutazioneMinima;

    public FiltroValutazioneMinima(FiltroLibro filtroPrecedente, int valutazioneMinima) {
        super(filtroPrecedente);
        this.valutazioneMinima = valutazioneMinima;
    }

    @Override
    public List<Libro> filtra(List<Libro> libri) {
        List<Libro> libriFiltrati = super.filtra(libri);
        return libriFiltrati.stream()
                .filter(libro -> libro.getValutazione() >= valutazioneMinima)
                .collect(Collectors.toList());
    }//filtra


}//FiltroValMin
