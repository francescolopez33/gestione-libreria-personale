package com.gestionelibreria.command;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GestoreComandiTest {

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
    }//setUp

    @Test
    void testEseguiComandoAggiungi() {
        AggiungiLibroCMD cmd = new AggiungiLibroCMD(libro);

        GestoreComandi.eseguiComando(cmd);

        assertEquals(1, libreria.getLibri().size(), "Libro deve essere stato aggiunto");
        assertEquals(1, new GestoreComandi().getStoricoSize(), "Storico deve contenere 1 comando");
    }//eseguiAggiungi


    @Test
    void testAnnullaUltimoComando() {
        AggiungiLibroCMD cmd = new AggiungiLibroCMD(libro);

        GestoreComandi.eseguiComando(cmd);
        assertEquals(1, libreria.getLibri().size());

        GestoreComandi.annullaUltimoComando();
        assertTrue(libreria.getLibri().isEmpty(), "Libro deve essere stato rimosso con undo");
        assertEquals(0, new GestoreComandi().getStoricoSize(), "Storico deve tornare vuoto");
    }//annullaUltimoComando


    @Test
    void testAnnullaSenzaComandi() {
        //nessun comando nello storico
        GestoreComandi.annullaUltimoComando();

        assertTrue(libreria.getLibri().isEmpty(), "Libreria deve rimanere vuota");
    }//annullaSenzacOMANDO

    @Test
    void testStoricoConPiuComandi() {
        Libro libro2 = new Libro.Builder()
                .titolo("Baudolino")
                .autore("Umberto Eco")
                .isbn("456")
                .build();


        GestoreComandi.eseguiComando(new AggiungiLibroCMD(libro));
        GestoreComandi.eseguiComando(new AggiungiLibroCMD(libro2));

        assertEquals(2, libreria.getLibri().size(), "Devono esserci due libri in libreria");
        assertEquals(2, new GestoreComandi().getStoricoSize(), "Storico deve avere due comandi");


        GestoreComandi.annullaUltimoComando();
        assertEquals(1, libreria.getLibri().size(), "Dopo undo deve rimanere solo il primo libro");
        assertEquals("Il Nome della Rosa", libreria.getLibri().get(0).getTitolo());
    }//storicoConPiu comandi




}//gestoreComandiTest
