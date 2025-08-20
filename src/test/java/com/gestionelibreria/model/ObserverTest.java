package com.gestionelibreria.model;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.observer.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    private Libreria libreria;
    private osservatoreTest ossTest;

    //Osservatore  per il test
    static class osservatoreTest implements Observer {
        private boolean isNotified = false;
        private List<Libro> libriRicevuti;

        @Override
        public void update(List<Libro> libri) {
            this.isNotified = true; //Segna notificato
            this.libriRicevuti = libri; //Salva i libri
        }//update

        public boolean isNotified() {
            return isNotified;
        }//isNot

        public List<Libro> getLibriRicevuti() {
            return libriRicevuti;
        }//getLibri

        public void reset() {
            this.isNotified = false;
            this.libriRicevuti = null;
        }//reset

    }//ossTest



    @BeforeEach
    void setUp() {
        libreria = Libreria.getInstance();
        libreria.svuotaLibreria(); //pulizia
        ossTest = new osservatoreTest();
        libreria.registraObserver(ossTest); //registra osservatore
    }//setup

    @Test
    void testObserverNotificatoDopoAggiuntaLibro() {
        //libro di test
        Libro libro = new Libro.Builder()
                .titolo("Test")
                .autore("Autore")
                .isbn("123")
                .build();


        libreria.aggiungiLibro(libro);

        //verifica
        assertTrue(ossTest.isNotified(), "L'Observer doveva essere notificato dopo l'aggiunta di un libro");


        assertNotNull(ossTest.getLibriRicevuti());
        assertEquals(1, ossTest.getLibriRicevuti().size());
        assertEquals("Test", ossTest.getLibriRicevuti().get(0).getTitolo());
    }//testObserverNotificatoDopoAggiuntaLibro


    @Test
    void testObserverNotificatoDopoRimozioneLibro() {

        Libro libro = new Libro.Builder().titolo("Test").autore("Autore").isbn("123").build();
        libreria.aggiungiLibro(libro);
        ossTest.reset();


        libreria.rimuoviLibro("123");


        assertTrue(ossTest.isNotified(), "L'Observer doveva essere notificato dopo la rimozione di un libro");
        assertTrue(ossTest.getLibriRicevuti().isEmpty()); //la lista ricevuta dovrebbe essere vuota
    }//testObserverNotificatoDopoRimozioneLibro


    @Test
    void testObserverNonNotificatoSeAggiuntaFallisce() {
        Libro libro1 = new Libro.Builder().titolo("Test1").autore("Autore").isbn("123").build();
        libreria.aggiungiLibro(libro1);
        ossTest.reset();
        Libro libro2 = new Libro.Builder().titolo("Test2").autore("Autore").isbn("123").build();
        boolean risultato = libreria.aggiungiLibro(libro2);


        assertFalse(risultato, "L'aggiunta di un ISBN duplicato doveva fallire");
        assertFalse(ossTest.isNotified(), "L'Observer NON doveva essere notificato se l'aggiunta è fallita");
    }//testObserverNonNotificatoSeAggiuntaFallisce


    @Test
    void testObserverNotificatoDopoModificaLibro() {
        Libro libro = new Libro.Builder().titolo("Vecchio Titolo").autore("Autore").isbn("111").build();
        libreria.aggiungiLibro(libro);
        ossTest.reset();

        Libro libroModificato = new Libro.Builder().titolo("Nuovo Titolo").autore("Autore").isbn("111").build();
        boolean risultato = libreria.modificaLibro(libroModificato);

        // Verifica
        assertTrue(risultato, "La modifica del libro doveva avere successo");
        assertTrue(ossTest.isNotified(), "L'Observer doveva essere notificato dopo la modifica di un libro");
        assertEquals("Nuovo Titolo", ossTest.getLibriRicevuti().get(0).getTitolo());
    }//testObserverNotificatoDopoModificaLibro

    @Test
    void testObserverNonNotificatoSeModificaFallisce() {
        ossTest.reset();

        //modificare un libro inesistente
        Libro libroInesistente = new Libro.Builder().titolo("Libro inesistente").autore("X").isbn("222").build();
        boolean risultato = libreria.modificaLibro(libroInesistente);

        //verifica
        assertFalse(risultato, "La modifica di un libro inesistente doveva fallire");
        assertFalse(ossTest.isNotified(), "L'Observer NON doveva essere notificato se la modifica fallisce");
    }//testObserverNonNotificatoSeModificaFallisce

    @Test
    void testObserverNonNotificatoSeRimosso() {

        libreria.rimuoviObserver(ossTest);

        Libro libro = new Libro.Builder().titolo("Libro Test").autore("Autore").isbn("333").build();
        libreria.aggiungiLibro(libro);

        assertFalse(ossTest.isNotified(), "L'Observer rimosso NON doveva ricevere notifiche");
    }//testObserverNonNotificatoSeRimosso


}//ObserverTest
