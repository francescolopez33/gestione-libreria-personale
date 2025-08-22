package com.gestionelibreria.strategy;


import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class OrdinamentoStrategyTest {

    private List<Libro> libri;

    @BeforeEach
    void setUp() {
        libri = new ArrayList<>();

        libri.add(new Libro.Builder()
                .titolo("Zorro")
                .autore("Carlos")
                .isbn("001")
                .valutazione(3)
                .stato(StatoLettura.IN_LETTURA)
                .build());

        libri.add(new Libro.Builder()
                .titolo("Anna Karenina")
                .autore("Tolstoj")
                .isbn("002")
                .valutazione(5)
                .stato(StatoLettura.LETTO)
                .build());

        libri.add(new Libro.Builder()
                .titolo("Il Nome della Rosa")
                .autore("Umberto Eco")
                .isbn("003")
                .valutazione(4)
                .stato(StatoLettura.DA_LEGGERE)
                .build());
    }//setUp



    @Test
    void testOrdinamentoPerTitolo() {
        OrdinamentoStrategy strategy = new OrdinamentoPerTitolo();
        List<Libro> ordinati = strategy.ordina(libri);

        assertEquals("Anna Karenina", ordinati.get(0).getTitolo());
        assertEquals("Il Nome della Rosa", ordinati.get(1).getTitolo());
        assertEquals("Zorro", ordinati.get(2).getTitolo());
    }//ordinamentoXtitolo


    @Test
    void testOrdinamentoPerAutore() {
        OrdinamentoStrategy strategy = new OrdinamentoPerAutore();
        List<Libro> ordinati = strategy.ordina(libri);

        assertEquals("Carlos", ordinati.get(0).getAutore());
        assertEquals("Tolstoj", ordinati.get(1).getAutore());
        assertEquals("Umberto Eco", ordinati.get(2).getAutore());
    }//ordinamentoXAutore



    @Test
    void testOrdinamentoPerValutazione() {
        OrdinamentoStrategy strategy = new OrdinamentoPerValutazione();
        List<Libro> ordinati = strategy.ordina(libri);

        assertEquals(5, ordinati.get(0).getValutazione());
        assertEquals(4, ordinati.get(1).getValutazione());
        assertEquals(3, ordinati.get(2).getValutazione());
    }//OrdinamentoXvalutazione


    @Test
    void testOrdinamentoPerStatoLettura() {
        OrdinamentoStrategy strategy = new OrdinamentoPerStatoLettura();
        List<Libro> ordinati = strategy.ordina(libri);

        assertEquals(StatoLettura.LETTO, ordinati.get(0).getStatoLettura());
        assertEquals(StatoLettura.IN_LETTURA, ordinati.get(1).getStatoLettura());
        assertEquals(StatoLettura.DA_LEGGERE, ordinati.get(2).getStatoLettura());
    }//OrdinamentoXstatoLett



    @Test
    void testListaVuota() {
        OrdinamentoStrategy strategy = new OrdinamentoPerTitolo();
        List<Libro> ordinati = strategy.ordina(new ArrayList<>());

        assertTrue(ordinati.isEmpty(), "L'ordinamento su lista vuota deve restituire lista vuota");
    }//listaVuota


    @Test
    void testUnSoloElemento() {
        List<Libro> singleton = List.of(new Libro.Builder().titolo("Unico").autore("Solo").isbn("NN").build());
        OrdinamentoStrategy strategy = new OrdinamentoPerValutazione();
        List<Libro> ordinati = strategy.ordina(singleton);

        assertEquals(1, ordinati.size());
        assertEquals("Unico", ordinati.get(0).getTitolo());
    }//Unelemento


    @Test
    void testElementiDuplicati() {
        List<Libro> duplicati = new ArrayList<>();
        duplicati.add(new Libro.Builder().titolo("Titolo").autore("Autore").isbn("1").build());
        duplicati.add(new Libro.Builder().titolo("Titolo").autore("Autore").isbn("2").build());

        OrdinamentoStrategy strategy = new OrdinamentoPerTitolo();
        List<Libro> ordinati = strategy.ordina(duplicati);

        assertEquals(2, ordinati.size());
        assertEquals("Titolo", ordinati.get(0).getTitolo());
        assertEquals("Titolo", ordinati.get(1).getTitolo());

    }//elementiDuplicati



}//OrdinamentoStrategyTest
