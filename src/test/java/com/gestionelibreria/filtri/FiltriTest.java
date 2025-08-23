package com.gestionelibreria.filtri;


import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;



public class FiltriTest {


    private List<Libro> libriDiTest;

    @BeforeEach
    void setUp() {
        libriDiTest = new ArrayList<>();

        //Valutazione alta, letto
        libriDiTest.add(new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Umberto Eco")
                .isbn("001")
                .genere("Giallo Storico")
                .valutazione(5)
                .stato(StatoLettura.LETTO)
                .build());

        //Valutazione media, in lettura
        libriDiTest.add(new Libro.Builder()
                .titolo("1984")
                .autore("George Orwell")
                .isbn("002")
                .genere("Distopico")
                .valutazione(3)
                .stato(StatoLettura.IN_LETTURA)
                .build());

        //Valutazione bassa, da leggere
        libriDiTest.add(new Libro.Builder()
                .titolo("Cime Tempestose")
                .autore("Emily Bronte")
                .isbn("003")
                .genere("Romanzo")
                .valutazione(4)
                .stato(StatoLettura.DA_LEGGERE)
                .build());

        //Stesso autore del primo, genere diverso
        libriDiTest.add(new Libro.Builder()
                .titolo("Il Pendolo di Foucault")
                .autore("Umberto Eco")
                .isbn("004")
                .genere("Saggio")
                .valutazione(4)
                .stato(StatoLettura.LETTO)
                .build());
    }//setUp





    @Test
    void testFiltroBaseListaVuota() {
        FiltroLibro filtro = new FiltroConcreto();
        List<Libro> risultato = filtro.filtra(new ArrayList<>());
        assertTrue(risultato.isEmpty());
    }//BaseVuoto

    @Test
    void testFiltroBaseListaPiena() {
        FiltroLibro filtro = new FiltroConcreto();
        List<Libro> risultato = filtro.filtra(libriDiTest);
        assertEquals(4, risultato.size());
    }//BasePieno



    @Test
    void testFiltroAutoreOk() { //per cercare libri di autore
        FiltroLibro filtro = new FiltroAutore(new FiltroConcreto(), "Umberto Eco");
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(2, risultato.size());
        assertTrue(risultato.stream().allMatch(l -> l.getAutore().equals("Umberto Eco")));
    }//autoreOk


    @Test
    void testFiltroAutoreNo() {
        FiltroLibro filtro = new FiltroAutore(new FiltroConcreto(), "Autore Inesistente");
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertTrue(risultato.isEmpty());
    }//AutoreNo


    @Test
    void testFiltroAutoreminuscolo() {
        FiltroLibro filtro = new FiltroAutore(new FiltroConcreto(), "umberto eco");
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(2, risultato.size());
    }//minuscolo


    @Test
    void testFiltroAutoreStringaVuota() {
        FiltroLibro filtro = new FiltroAutore(new FiltroConcreto(), "");
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertTrue(risultato.isEmpty());
    }//autoreVuoto





    @Test
    void testFiltroGenereOK() {
        FiltroLibro filtro = new FiltroGenere(new FiltroConcreto(), "Giallo Storico");
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(1, risultato.size());
        assertEquals("Il Nome della Rosa", risultato.get(0).getTitolo());
    }//genereOK


    @Test
    void testFiltroGenereNo() {
        FiltroLibro filtro = new FiltroGenere(new FiltroConcreto(), "Horror");
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertTrue(risultato.isEmpty());
    }//genereNO


    @Test
    void testFiltroValutazioneMinima() {
        FiltroLibro filtro = new FiltroValutazioneMinima(new FiltroConcreto(), 4);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(3, risultato.size());
        assertTrue(risultato.stream().allMatch(l -> l.getValutazione() >= 4));
    }//ValMin


    @Test
    void testFiltroValutazioneMinimaLimiteMassimo() {
        FiltroLibro filtro = new FiltroValutazioneMinima(new FiltroConcreto(), 5);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(1, risultato.size());
        assertEquals(5, risultato.get(0).getValutazione());
    }//ValMinMax



    @Test
    void testFiltroValutazioneMinimaSottoLimite() {
        FiltroLibro filtro = new FiltroValutazioneMinima(new FiltroConcreto(), 1);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(4, risultato.size()); // Tutti i libri
    }//ValMinMin


    @Test
    void testFiltroValutazioneMinimaSopraLimite() {
        FiltroLibro filtro = new FiltroValutazioneMinima(new FiltroConcreto(), 6);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertTrue(risultato.isEmpty());
    }//valMinSopra


    @Test
    void testFiltroValutazioneMassima() {
        FiltroLibro filtro = new FiltroValutazioneMassima(new FiltroConcreto(), 3);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(1, risultato.size());
        assertEquals(3, risultato.get(0).getValutazione());
    }//valMAX





    @Test
    void testFiltroStatoLetturaOK() {
        FiltroLibro filtro = new FiltroStatoLettura(new FiltroConcreto(), StatoLettura.LETTO);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(2, risultato.size());
        assertTrue(risultato.stream().allMatch(l -> l.getStatoLettura() == StatoLettura.LETTO));
    }//LetturaOk


    @Test
    void testFiltroStatoLetturaNo() {
        FiltroLibro filtro = new FiltroStatoLettura(new FiltroConcreto(), StatoLettura.IN_LETTURA);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(1, risultato.size());
        assertEquals("1984", risultato.get(0).getTitolo());
    }//letturaNo



    @Test
    void testFiltriCombinatiAutoreEValutazione() {
        FiltroLibro filtro = new FiltroValutazioneMinima(
                new FiltroAutore(
                        new FiltroConcreto(),
                        "Umberto Eco"),
                4);

        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertEquals(2, risultato.size());
        assertTrue(risultato.stream().allMatch(l ->
                l.getAutore().equals("Umberto Eco") && l.getValutazione() >= 4));
    }//AutoreEvalutazione



    @Test
    void testFiltriCombinatiMultipliSenzaRisultati() {
        FiltroLibro filtro = new FiltroValutazioneMinima(
                new FiltroAutore(
                        new FiltroGenere(
                                new FiltroConcreto(),
                                "Horror"),
                        "Umberto Eco"),
                5);

        List<Libro> risultato = filtro.filtra(libriDiTest);
        assertTrue(risultato.isEmpty());
    }//FiltriMultipli


    @Test
    void testFiltroConListaNull() {
        FiltroLibro filtro = new FiltroConcreto();
        List<Libro> risultato = filtro.filtra(null);

        assertNull(risultato);
    }//ListaNull


    @Test
    void testFiltroAutoreNull() {
        FiltroLibro filtro = new FiltroAutore(new FiltroConcreto(), null);
        List<Libro> risultato = filtro.filtra(libriDiTest);

        assertTrue(risultato.isEmpty());
    }//AutoreNull


}//filtriTest
