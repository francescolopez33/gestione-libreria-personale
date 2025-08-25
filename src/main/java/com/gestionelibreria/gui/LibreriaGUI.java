package com.gestionelibreria.gui;

import com.gestionelibreria.command.AggiungiLibroCMD;
import com.gestionelibreria.command.GestoreComandi;
import com.gestionelibreria.command.ModificaLibroCMD;
import com.gestionelibreria.command.RimuoviLibroCMD;
import com.gestionelibreria.filtri.*;
import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.mediator.LibreriaMediator;
import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;
import com.gestionelibreria.observer.Observer;
import com.gestionelibreria.persistenza.FileLibreriaRepository;
import com.gestionelibreria.strategy.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class LibreriaGUI extends JFrame implements Observer{

    private JTable tabella;
    public DefaultTableModel tabellaModel;


    public JTextField filtroField;
    public JComboBox<String> filtroBox;
    public JComboBox<String> ordinamentoBox;

    public JTextField cercaField;

    private final LibreriaMediator mediator;

    public LibreriaGUI() {
        setTitle("Gestione Libreria Personale");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.mediator=new LibreriaMediator(this);
        mediator.caricaLibreria();

        //tabella
        tabellaModel = new DefaultTableModel(
                new Object[]{"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato", "Opzioni"}, 0
        ) {
            @Override
            public boolean isCellEditable(int riga, int col) {
                return col== 6;
            }
        };

        tabella = new JTable(tabellaModel);
        tabella.setFillsViewportHeight(true);
        tabella.setRowHeight(25);
        tabella.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane pannelloScroll = new JScrollPane(tabella);
        add(pannelloScroll, BorderLayout.CENTER);


        tabella.getColumn("Opzioni").setCellRenderer(new BottoneRender());
        tabella.getColumn("Opzioni").setCellEditor(new BottoneEditor(new JCheckBox(), this));


        JPanel topPanel = new JPanel(new BorderLayout());

        //Pannello per la ricerca singola
        JPanel cercaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cercaField = new JTextField(20);
        JButton cercaB = new JButton("Cerca");


        cercaPanel.add(new JLabel("Ricerca: "));
        cercaPanel.add(cercaField);
        cercaPanel.add(cercaB);
        topPanel.add(cercaPanel, BorderLayout.NORTH);

        // Pannello per i filtri e l'ordinamento avanzati (implementazione esistente)
        JPanel filtroOrdinamentoPanel = new JPanel();
        filtroOrdinamentoPanel.setLayout(new BoxLayout(filtroOrdinamentoPanel, BoxLayout.X_AXIS));

        filtroBox = new JComboBox<>(new String[]{"Nessuno", "Titolo", "Autore", "Genere"});
        filtroField = new JTextField(20);
        ordinamentoBox = new JComboBox<>(new String[]{"Nessuno", "Titolo", "Autore", "Valutazione", "Stato Lettura"});

        filtroOrdinamentoPanel.add(new JLabel("Filtra: "));
        filtroOrdinamentoPanel.add(filtroBox);
        filtroOrdinamentoPanel.add(Box.createHorizontalStrut(10)); // spaziatura
        filtroOrdinamentoPanel.add(new JLabel("Testo: "));
        filtroOrdinamentoPanel.add(filtroField);
        filtroOrdinamentoPanel.add(Box.createHorizontalStrut(10));
        filtroOrdinamentoPanel.add(new JLabel("Ordina: "));
        filtroOrdinamentoPanel.add(ordinamentoBox);

        topPanel.add(filtroOrdinamentoPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);


        JPanel bottone = new JPanel(new BorderLayout());


        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton aggiungiB = new JButton("Aggiungi");
        //JButton aggiornaB = new JButton("Modifica");
        //JButton eliminaB = new JButton("Rimuovi");
        JButton indietroB = new JButton("Indietro");
        pannelloBottoni.add(aggiungiB);
        //pannelloBottoni.add(aggiornaB);
        //pannelloBottoni.add(eliminaB);
        pannelloBottoni.add(indietroB);


        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton salvaB = new JButton("Salva");
        JButton caricaB = new JButton("Carica");
        filePanel.add(salvaB);
        filePanel.add(caricaB);


        bottone.add(pannelloBottoni, BorderLayout.WEST);
        bottone.add(filePanel, BorderLayout.EAST);
        add(bottone, BorderLayout.SOUTH);



        aggiungiB.addActionListener(e -> mediator.onAggiungiClicked());
        //aggiornaB.addActionListener(e -> modificaLibro());
        //eliminaB.addActionListener(e -> rimuoviLibro());
        indietroB.addActionListener(e -> mediator.onIndietroClicked());
        salvaB.addActionListener(e -> mediator.onSalvaClicked());
        caricaB.addActionListener(e -> mediator.onCaricaClicked());
        cercaB.addActionListener(e -> mediator.onCercaClicked());
        cercaField.addActionListener(e -> mediator.onCercaClicked());


        filtroField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { aggiornaVista(); }
            public void removeUpdate(DocumentEvent e) { aggiornaVista(); }
            public void insertUpdate(DocumentEvent e) { aggiornaVista(); }
        });
        filtroBox.addActionListener(e -> mediator.onFiltroOrdinamentoChanged());
        ordinamentoBox.addActionListener(e -> mediator.onFiltroOrdinamentoChanged());


        Libreria.getInstance().registraObserver(this);
        mediator.aggiornaVista();
    }//LibGUI



    @Override
    public void update(List<Libro> libri) {

        mediator.aggiornaVista();
    }//update



    private void aggiornaVista() {
        mediator.aggiornaVista();
    }//aggiornaVisita



    void modificaLibro(int riga) {
        mediator.onModificaClicked(riga);
    }//modificaLibro


    void rimuoviLibro(int riga) {
        mediator.onRimuoviClicked(riga);
    }//rimuoviLibro


}//LibreriaGUI
