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
import java.util.ArrayList;
import java.util.List;

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


    /*
    @Test
    void testAggiornaVistaConFiltriEOrdinamento() {

        JComboBox<String> mockOrdinamentoBox = mock(JComboBox.class);
        JTextField mockFiltroGenere = mock(JTextField.class);
        JComboBox<StatoLettura> mockFiltroStato = mock(JComboBox.class);
        JTextField mockFiltroValMin = mock(JTextField.class);
        JTextField mockFiltroValMax = mock(JTextField.class);
        JTextField mockCercaField = mock(JTextField.class);


        when(mockGui.getOrdinamentoBox()).thenReturn(mockOrdinamentoBox);
        when(mockGui.getFiltroGenere()).thenReturn(mockFiltroGenere);
        when(mockGui.getFiltroStato()).thenReturn(mockFiltroStato);
        when(mockGui.getFiltroValMin()).thenReturn(mockFiltroValMin);
        when(mockGui.getFiltroValMax()).thenReturn(mockFiltroValMax);
        when(mockGui.getCercaField()).thenReturn(mockCercaField);


        when(mockOrdinamentoBox.getSelectedItem()).thenReturn("Nessuno");
        when(mockFiltroGenere.getText()).thenReturn("");
        when(mockFiltroStato.getSelectedItem()).thenReturn(null);
        when(mockFiltroValMin.getText()).thenReturn("");
        when(mockFiltroValMax.getText()).thenReturn("");
        when(mockCercaField.getText()).thenReturn("");


        Libro libro1 = new Libro.Builder()
                .titolo("Il Signore degli Anelli")
                .autore("J.R.R. Tolkien")
                .isbn("123")
                .genere("Fantasy")
                .valutazione(5)
                .stato(StatoLettura.LETTO)
                .build();

        when(mockLibreria.getLibri()).thenReturn(List.of(libro1));


        mediator.aggiornaVista();

        verify(mockTableModel).setRowCount(0);
        verify(mockTableModel).addRow(any(Object[].class));
    }//AggiornaVisitaTEST

*/




    @Test
    void testRimuoviLibroSelezionato() {

        int rigaSelezionata = 0;
        String isbnDaRimuovere = "123-456";


        when(mockTableModel.getValueAt(rigaSelezionata, 2)).thenReturn(isbnDaRimuovere);


        try (MockedStatic<GestoreComandi> mockedGestore = mockStatic(GestoreComandi.class)) {

            mediator.rimuoviLibroSelezionato(rigaSelezionata);


            mockedGestore.verify(() -> GestoreComandi.eseguiComando(argThat(
                    cmd -> cmd instanceof RimuoviLibroCMD &&
                            ((RimuoviLibroCMD) cmd).getIsbn().equals(isbnDaRimuovere))));
        }
    }//testRimuoviLibroSelezionato



}//LibreriaMediatorTest
