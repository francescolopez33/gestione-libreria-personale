package com.gestionelibreria.model;

import com.gestionelibreria.libreria.Libreria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class LibreriaTest {

    private Libreria libreria;
    private Libro libro1;

    @BeforeEach
    void setUp() {
        //istanza Singleton e svuotarla prima di ogni test
        libreria = Libreria.getInstance();
        libreria.svuotaLibreria();

        libro1 = new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Umberto Eco")
                .isbn("123")
                .build();
    }//setup

    @Test
    void testSingleton() {
        Libreria firstInstance = Libreria.getInstance();
        Libreria secondInstance = Libreria.getInstance();
        assertSame(firstInstance, secondInstance, "Le istanze Singleton dovrebbero essere la stessa");
    }//testSingleton

    @Test
    void testAggiungiLibroValido() {
        libreria.aggiungiLibro(libro1);
        assertEquals(1, libreria.getLibri().size());
    }//testAggiungiLibroOk

    @Test
    void testAggiungiLibroDuplicato() {
        libreria.aggiungiLibro(libro1);

        Libro libroDuplicato = new Libro.Builder()
                .titolo("Altro Titolo")
                .autore("Altro Autore")
                .isbn("123") // stesso ISBN
                .build();

        assertThrows(IllegalArgumentException.class, () -> libreria.aggiungiLibro(libroDuplicato));


        assertEquals(1, libreria.getLibri().size());
    }//testAggiungiDuplicato


    @Test
    void testGetLibrinonModificabile() {//per verificare che la lista restituita sia effettivamente non modifi
        assertThrows(UnsupportedOperationException.class, () -> {
            libreria.getLibri().add(libro1);
        });
    }//testListaNonmod


    @Test
    void testRimuoviLibroEsistente() {
        libreria.aggiungiLibro(libro1);
        boolean risultato = libreria.rimuoviLibro("123");
        assertTrue(risultato);
        assertEquals(0, libreria.getLibri().size());
    }//rimuovio libro esistente


    @Test
    void testRimuoviLibroInesistente() {
        boolean risultato = libreria.rimuoviLibro("000");
        assertFalse(risultato);
        assertEquals(0, libreria.getLibri().size());
    }//timuoviInestistente


    @Test
    void testModificaLibroEsistente() {
        libreria.aggiungiLibro(libro1);

        Libro libroModificato = new Libro.Builder()
                .titolo("Il Nome della Rosa - Edizione Speciale")
                .autore("Umberto Eco")
                .isbn("123") // stesso ISBN
                .build();

        boolean risultato = libreria.modificaLibro(libroModificato);
        assertTrue(risultato);
        assertEquals("Il Nome della Rosa - Edizione Speciale", libreria.getLibri().get(0).getTitolo());
    }//modificaEsistente



    @Test
    void testModificaLibroInesistente() {
        Libro libroModificato = new Libro.Builder()
                .titolo("Libro NO")
                .autore("Autore")
                .isbn("000") //non presente
                .build();

        boolean risultato = libreria.modificaLibro(libroModificato);
        assertFalse(risultato);
    }//modificaInesistente


}//LibreriaTest
