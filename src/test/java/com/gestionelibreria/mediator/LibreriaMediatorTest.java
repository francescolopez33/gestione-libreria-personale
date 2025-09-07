package com.gestionelibreria.mediator;

import com.gestionelibreria.command.AggiungiLibroCMD;
import com.gestionelibreria.command.GestoreComandi;
import com.gestionelibreria.command.RimuoviLibroCMD;
import com.gestionelibreria.gui.LibreriaGUI;
import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibreriaMediatorTest {

    @Mock
    private LibreriaGUI mockGui;
    @Mock
    private Libreria mockLibreria;
    @Mock
    private DefaultTableModel mockTableModel;


    @InjectMocks
    private LibreriaMediator mediator;

    @BeforeEach
    void setUp() {

        lenient().when(mockGui.getTabellaModel()).thenReturn(mockTableModel);

        try (MockedStatic<Libreria> mockedLibreria = mockStatic(Libreria.class)) {
            mockedLibreria.when(Libreria::getInstance).thenReturn(mockLibreria);

        }
    }//setUP



    @Test
    void testAggiuntaLibroSuccesso() {
        try (MockedStatic<JOptionPane> mockedOptionPane = mockStatic(JOptionPane.class)) {
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(), eq("Titolo:"))).thenReturn("Il Signore degli Anelli");
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(), eq("Autore:"))).thenReturn("J.R.R. Tolkien");
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(), eq("ISBN:"))).thenReturn("978-0-618-05326-7");
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(), eq("Genere:"))).thenReturn("Fantasy");
            mockedOptionPane.when(() -> JOptionPane.showInputDialog(any(), eq("Valutazione (1-5):"))).thenReturn("5");


            mockedOptionPane.when(() -> JOptionPane.showConfirmDialog(any(), any(JComboBox.class), anyString(), anyInt())).thenReturn(JOptionPane.OK_OPTION);


            try (MockedStatic<GestoreComandi> mockedGestore = mockStatic(GestoreComandi.class)) {

                mediator.aggiuntaLibro();

                mockedGestore.verify(() -> GestoreComandi.eseguiComando(any(AggiungiLibroCMD.class)));


                mockedOptionPane.verify(() -> JOptionPane.showMessageDialog(any(), eq("Libro aggiunto con successo!")));
            }
        }
    }//AggiungiLibroOK









}//LibreriaMediatorTest
