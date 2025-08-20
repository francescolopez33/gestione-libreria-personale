package com.gestionelibreria.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibroTest {

    @Test
    void testCreazioneLibroValido() {
        // Arrange & Act
        Libro libro = new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Umberto Eco")
                .isbn("9788845208528")
                .genere("Giallo Storico")
                .valutazione(5)
                .stato(StatoLettura.LETTO)
                .build();

        // Assert
        assertNotNull(libro);
        assertEquals("Il Nome della Rosa", libro.getTitolo());
        assertEquals("Umberto Eco", libro.getAutore());
        assertEquals(5, libro.getValutazione());
        assertEquals(StatoLettura.LETTO, libro.getStatoLettura());
    }

    @Test
    void testCreazioneLibroSenzaTitolo() {
        // Assert che si aspetti un'eccezione
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro.Builder()
                    .autore("Umberto Eco") // Manca titolo!
                    .isbn("9788845208528")
                    .build();
        });
    }

    @Test
    void testCreazioneLibroSenzaAutore() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro.Builder()
                    .titolo("Il Nome della Rosa")
                    // Manca autore!
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
                    .valutazione(6) // Troppo alta!
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
                .titolo("Titolo 2") // Titolo diverso
                .autore("Autore 2") // Autore diverso
                .isbn("1234567890") // Stesso ISBN
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
}