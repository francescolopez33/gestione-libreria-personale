package com.gestionelibreria.command;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    private Libreria libreria;
    private Libro libro;

    @BeforeEach
    void setUp() {
        libreria = Libreria.getInstance();
        libreria.svuotaLibreria();

        libro = new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Umberto Eco")
                .isbn("123")
                .build();
    }//setuo






    @Test
    void testAggiungiLibroCMD() {
        AggiungiLibroCMD cmd = new AggiungiLibroCMD(libro);
        cmd.esegui();

        assertEquals(1, libreria.getLibri().size());
        assertEquals("Il Nome della Rosa", libreria.getLibri().get(0).getTitolo());

        cmd.annulla();
        assertTrue(libreria.getLibri().isEmpty(), "Undo deve rimuovere il libro appena aggiunto");
    }//aggiungiLibro



    @Test
    void testAggiungiLibroCMDIsbnDuplicato() {
        Libreria libreria = Libreria.getInstance();
        libreria.svuotaLibreria();

        Libro libroDuplicato = new Libro.Builder()
                .titolo("Il Signore degli Anelli")
                .autore("J.R.R. Tolkien")
                .isbn("978-8845292613")
                .build();
        libreria.aggiungiLibro(libroDuplicato);


        assertThrows(IllegalArgumentException.class, () -> {
            libreria.aggiungiLibro(libroDuplicato);
        }, "L'aggiunta di un libro con ISBN duplicato deve lanciare un'IllegalArgumentException.");
    }//aggiungiLibroDuplicato



    @Test
    void testRimuoviLibroCMD() {
        libreria.aggiungiLibro(libro);

        RimuoviLibroCMD cmd = new RimuoviLibroCMD("123");
        cmd.esegui();

        assertTrue(libreria.getLibri().isEmpty(), "Libro deve essere rimosso");

        cmd.annulla();
        assertEquals(1, libreria.getLibri().size(), "Undo deve reinserire il libro rimosso");
    }//rimuovilibro






}//commandTest

