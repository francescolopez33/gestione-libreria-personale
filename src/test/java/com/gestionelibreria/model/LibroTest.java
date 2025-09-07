package com.gestionelibreria.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibroTest {

    @Test
    void testCreazioneLibroValido() {

        Libro libro = new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Umberto Eco")
                .isbn("9788845208528")
                .genere("Giallo Storico")
                .valutazione(5)
                .stato(StatoLettura.LETTO)
                .build();

        //assert
        assertNotNull(libro);
        assertEquals("Il Nome della Rosa", libro.getTitolo());
        assertEquals("Umberto Eco", libro.getAutore());
        assertEquals(5, libro.getValutazione());
        assertEquals(StatoLettura.LETTO, libro.getStatoLettura());
    }

    @Test
    void testCreazioneLibroSenzaTitolo() {


        assertThrows(IllegalArgumentException.class, () -> {
            new Libro.Builder()
                    .autore("Umberto Eco")
                    .isbn("9788845208528")
                    .build();
        });
    }

    @Test
    void testCreazioneLibroSenzaAutore() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro.Builder()
                    .titolo("Il Nome della Rosa")
                    .isbn("9788845208528")
                    .build();
        });
    }

    @Test
    void testValutazioneFuoriRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro.Builder()
                    .titolo("Il Nome della Rosa")
                    .autore("Umberto Eco")
                    .isbn("9788845208528")
                    .valutazione(6)
                    .build();
        });
    }

    @Test
    void testEqualsConStessoISBN() {
        Libro libro1 = new Libro.Builder()
                .titolo("Titolo 1")
                .autore("Autore 1")
                .isbn("1234567890")
                .build();

        Libro libro2 = new Libro.Builder()
                .titolo("Titolo 2")
                .autore("Autore 2")
                .isbn("1234567890")
                .build();

        assertTrue(libro1.equals(libro2));
    }

    @Test
    void testHashCodeConsistente() {
        Libro libro = new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Umberto Eco")
                .isbn("9788845208528")
                .build();

        assertEquals(libro.hashCode(), libro.hashCode());
    }

}//LibroTest