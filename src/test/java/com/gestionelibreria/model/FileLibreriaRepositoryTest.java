package com.gestionelibreria.model;


import com.gestionelibreria.persistenza.FileLibreriaRepository;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileLibreriaRepositoryTest {

    private FileLibreriaRepository repository;
    private static final String TEST_FILE = "libreria.json";

    @BeforeEach
    void setUp() {
        repository = FileLibreriaRepository.getInstance();
        //Pulizia
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }//setup


    @Test
    void testSalvaELeggiListaVuota() {
        repository.salva(List.of());
        List<Libro> caricati = repository.carica();
        assertTrue(caricati.isEmpty(), "Una lista vuota deve rimanere vuota dopo il salvataggio/caricamento");
    }//salvaleggiListavuota


    @Test
    void testSalvaELeggiUnLibro() {
        Libro libro = new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Eco")
                .isbn("123")
                .build();

        repository.salva(List.of(libro));
        List<Libro> caricati = repository.carica();

        assertEquals(1, caricati.size());
        assertEquals("123", caricati.get(0).getIsbn());
    }//salvaleggilibro



    @Test
    void testFileInesistenteRitornaListaVuota() {
        List<Libro> caricati = repository.carica();
        assertTrue(caricati.isEmpty(), "Se il file non esiste, deve restituire lista vuota");
    }//fileInesistente


    @Test
    void testSovrascriveFileConNuoviLibri() {
        Libro libro1 = new Libro.Builder().titolo("Libro1").autore("A").isbn("111").build();
        Libro libro2 = new Libro.Builder().titolo("Libro2").autore("B").isbn("222").build();

        repository.salva(List.of(libro1));
        repository.salva(List.of(libro1, libro2));

        List<Libro> caricati = repository.carica();
        assertEquals(2, caricati.size(), "Il file deve contenere solo i libri dell'ultimo salvataggio");
    }//SovrascriviConNuovi




}//filelibreriarepositorytest
