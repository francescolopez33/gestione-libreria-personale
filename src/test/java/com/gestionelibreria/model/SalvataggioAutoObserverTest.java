package com.gestionelibreria.model;

import com.gestionelibreria.libreria.Libreria;

import com.gestionelibreria.observer.SalvataggioAutoObserver;
import com.gestionelibreria.persistenza.FileLibreriaRepository;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SalvataggioAutoObserverTest {

    private Libreria libreria;
    private File file;

    @BeforeEach
    void setUp() {
        libreria = Libreria.getInstance();
        libreria.svuotaLibreria(); //reset libreria
        libreria.registraObserver(new SalvataggioAutoObserver());

        file = new File("libreria.json");
        if (file.exists()) {
            file.delete();
        }
    }//setUp

    @Test
    void testSalvataggioDopoAggiuntaLibro() {
        Libro libro = new Libro.Builder().titolo("Test").autore("Autore").isbn("123").build();
        libreria.aggiungiLibro(libro);

        assertTrue(file.exists(), "Il file JSON deve essere creato dopo un'aggiunta");
        List<Libro> caricati = FileLibreriaRepository.getInstance().carica();
        assertEquals(1, caricati.size());
    }//SalvataggioDopoAggiunta


    @Test
    void testSalvataggioDopoRimozioneLibro() {
        Libro libro = new Libro.Builder().titolo("Test").autore("Autore").isbn("123").build();
        libreria.aggiungiLibro(libro);
        libreria.rimuoviLibro("123");

        List<Libro> caricati = FileLibreriaRepository.getInstance().carica();
        assertTrue(caricati.isEmpty(), "Dopo la rimozione il file deve riflettere la lista vuota");
    }//SalvataggioDopoRimozione


    @Test
    void testNessunSalvataggioSeAggiuntaFallisce() {
        Libro libro1 = new Libro.Builder().titolo("Uno").autore("A").isbn("111").build();
        Libro libro2 = new Libro.Builder().titolo("Due").autore("B").isbn("111").build(); // stesso ISBN

        libreria.aggiungiLibro(libro1);
        file.delete(); // reset dopo primo salvataggio
        libreria.aggiungiLibro(libro2); // dovrebbe fallire

        assertFalse(file.exists(), "Se l'aggiunta fallisce, il file non deve essere modificato");
    }//NosalvataggioDopoFallimento


    @Test
    void testSalvataggioDopoModificaLibro() {
        Libro libro = new Libro.Builder().titolo("Originale").autore("A").isbn("999").build();
        libreria.aggiungiLibro(libro);

        Libro modificato = new Libro.Builder().titolo("Modificato").autore("A").isbn("999").build();
        libreria.modificaLibro(modificato);

        List<Libro> caricati = FileLibreriaRepository.getInstance().carica();
        assertEquals("Modificato", caricati.get(0).getTitolo(), "Il file deve contenere la versione aggiornata del libro");
    }//Salvattaggio dopo modifica





}//SalvataggioAutoObserverTEST
