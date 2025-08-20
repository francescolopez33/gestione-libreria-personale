package com.gestionelibreria.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatoLetturaTest {

    @Test
    void testValoriEnum() {
        // Verifica che tutti i valori esistano
        assertNotNull(StatoLettura.valueOf("LETTO"));
        assertNotNull(StatoLettura.valueOf("IN_LETTURA"));
        assertNotNull(StatoLettura.valueOf("DA_LEGGERE"));

        assertEquals(3, StatoLettura.values().length);
    }
}
