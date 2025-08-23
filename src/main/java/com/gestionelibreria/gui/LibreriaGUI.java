package com.gestionelibreria.gui;

import com.gestionelibreria.command.AggiungiLibroCMD;
import com.gestionelibreria.command.GestoreComandi;
import com.gestionelibreria.command.ModificaLibroCMD;
import com.gestionelibreria.command.RimuoviLibroCMD;
import com.gestionelibreria.filtri.*;
import com.gestionelibreria.libreria.Libreria;
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

public class LibreriaGUI extends JFrame implements Observer{

    private JTable tabella;
    private DefaultTableModel tabellaModel;


    private JTextField filtroField;
    private JComboBox<String> filtroComboBox;
    private JComboBox<String> ordinamentoComboBox;


    public LibreriaGUI() {
        setTitle("Gestione Libreria Personale");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        caricaLibreria();

        //tabella
        tabellaModel = new DefaultTableModel(
                new Object[]{"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato"}, 0
        );
        tabella = new JTable(tabellaModel);
        JScrollPane scrollPane = new JScrollPane(tabella);
        add(scrollPane, BorderLayout.CENTER);



        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        filtroField = new JTextField(20);
        filtroComboBox = new JComboBox<>(new String[]{"Nessuno", "Titolo", "Autore", "Genere"});
        ordinamentoComboBox = new JComboBox<>(new String[]{"Nessuno", "Titolo", "Autore", "Valutazione", "Stato Lettura"});

        topPanel.add(new JLabel("Filtra per:"));
        topPanel.add(filtroComboBox);
        topPanel.add(new JLabel("Termine:"));
        topPanel.add(filtroField);
        topPanel.add(new JLabel("Ordina per:"));
        topPanel.add(ordinamentoComboBox);

        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Aggiungi");
        JButton updateButton = new JButton("Modifica");
        JButton deleteButton = new JButton("Rimuovi");
        JButton undoButton = new JButton("Undo");
        JButton saveButton = new JButton("Salva");
        JButton loadButton = new JButton("Carica");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        add(buttonPanel, BorderLayout.SOUTH);


        addButton.addActionListener(e -> aggiungiLibro());
        updateButton.addActionListener(e -> modificaLibro());
        deleteButton.addActionListener(e -> rimuoviLibro());
        undoButton.addActionListener(e -> GestoreComandi.annullaUltimoComando());
        saveButton.addActionListener(e -> salvaLibreria());
        loadButton.addActionListener(e -> caricaLibreria());


        filtroField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { aggiornaVista(); }
            public void removeUpdate(DocumentEvent e) { aggiornaVista(); }
            public void insertUpdate(DocumentEvent e) { aggiornaVista(); }
        });
        filtroComboBox.addActionListener(e -> aggiornaVista());
        ordinamentoComboBox.addActionListener(e -> aggiornaVista());


        Libreria.getInstance().registraObserver(this);


        aggiornaVista();
    }//LibGUI



    @Override
    public void update(List<Libro> libri) {
        aggiornaVista();
    }//update



    private void aggiornaTabella(List<Libro> libri) {
        tabellaModel.setRowCount(0);
        for (Libro l : libri) {
            tabellaModel.addRow(new Object[]{
                    l.getTitolo(),
                    l.getAutore(),
                    l.getIsbn(),
                    l.getGenere(),
                    l.getValutazione(),
                    l.getStatoLettura()
            });
        }
    }//AggiornaTabella


    private void aggiornaVista() {
        List<Libro> libri = Libreria.getInstance().getLibri();

        String filtroTesto = filtroField.getText().trim();
        String filtroTipo = (String) filtroComboBox.getSelectedItem();

        if (!"Nessuno".equals(filtroTipo) && !filtroTesto.isEmpty()) {
            FiltroLibro filtroCorrente = new FiltroConcreto();
            if ("Titolo".equals(filtroTipo)) {

                libri = new FiltroTitolo(filtroCorrente, filtroTesto).filtra(libri);
            } else if ("Autore".equals(filtroTipo)) {

                libri = new FiltroAutore(filtroCorrente, filtroTesto).filtra(libri);
            } else if ("Genere".equals(filtroTipo)) {

                libri = new FiltroGenere(filtroCorrente, filtroTesto).filtra(libri);
            }
        }


        OrdinamentoStrategy strategy = null;
        String ordinamentoTipo = (String) ordinamentoComboBox.getSelectedItem();
        switch (ordinamentoTipo) {
            case "Titolo":
                strategy = new OrdinamentoPerTitolo();
                break;
            case "Autore":
                strategy = new OrdinamentoPerAutore();
                break;
            case "Valutazione":
                strategy = new OrdinamentoPerValutazione();
                break;
            case "Stato Lettura":
                strategy = new OrdinamentoPerStatoLettura();
                break;
        }

        if (strategy != null) {
            libri = strategy.ordina(libri);
        }


        aggiornaTabella(libri);
    }//aggiornaVisita


    private void aggiungiLibro() {
        try{
        String titolo = JOptionPane.showInputDialog(this, "Titolo:");
        String autore = JOptionPane.showInputDialog(this, "Autore:");
        String isbn = JOptionPane.showInputDialog(this, "ISBN:");
        String genere = JOptionPane.showInputDialog(this, "Genere:");
        int valutazione = Integer.parseInt(JOptionPane.showInputDialog(this, "Valutazione (1-5):"));
        StatoLettura stato = StatoLettura.valueOf(
                JOptionPane.showInputDialog(this, "Stato (LETTO, IN_LETTURA, DA_LEGGERE):")
                        .toUpperCase()
        );

        Libro libro = new Libro.Builder()
                .titolo(titolo)
                .autore(autore)
                .isbn(isbn)
                .genere(genere)
                .valutazione(valutazione)
                .stato(stato)
                .build();

        GestoreComandi.eseguiComando(new AggiungiLibroCMD(libro));

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Errore di input. Controlla i valori inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }//aggiungiLibro



    private void modificaLibro() {
        int row = tabella.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da modificare.");
            return;
        }


        try{
        String isbn = (String) tabellaModel.getValueAt(row, 2);
        String nuovoTitolo = JOptionPane.showInputDialog(this, "Nuovo titolo:");
        String nuovoAutore = JOptionPane.showInputDialog(this, "Nuovo autore:");
        String nuovoGenere = JOptionPane.showInputDialog(this, "Nuovo genere:");
        int nuovaValutazione = Integer.parseInt(JOptionPane.showInputDialog(this, "Nuova valutazione (1-5):"));
        StatoLettura nuovoStato = StatoLettura.valueOf(
                JOptionPane.showInputDialog(this, "Nuovo stato (LETTO, IN_LETTURA, DA_LEGGERE):")
                        .toUpperCase()
        );

        Libro libroModificato = new Libro.Builder()
                .titolo(nuovoTitolo)
                .autore(nuovoAutore)
                .isbn(isbn)
                .genere(nuovoGenere)
                .valutazione(nuovaValutazione)
                .stato(nuovoStato)
                .build();

        GestoreComandi.eseguiComando(new ModificaLibroCMD(libroModificato));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Errore di input. Controlla i valori inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }//modificaLibro


    private void rimuoviLibro() {
        int row = tabella.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere.");
            return;
        }

        String isbn = (String) tabellaModel.getValueAt(row, 2);
        GestoreComandi.eseguiComando(new RimuoviLibroCMD(isbn));
    }//rimuoviLibro

    private void salvaLibreria() {
        FileLibreriaRepository.getInstance().salva(Libreria.getInstance().getLibri());

    }//salvaLibreria

    private void caricaLibreria() {
        List<Libro> libriCaricati = FileLibreriaRepository.getInstance().carica();
        Libreria.getInstance().setLibri(libriCaricati);
        JOptionPane.showMessageDialog(this, "Libreria caricata con successo!");
    }//caricaLibreria


}//LibreriaGUI
