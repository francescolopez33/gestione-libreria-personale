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
import java.util.stream.Collectors;

public class LibreriaGUI extends JFrame implements Observer{

    private JTable tabella;
    private DefaultTableModel tabellaModel;


    private JTextField filtroField;
    private JComboBox<String> filtroBox;
    private JComboBox<String> ordinamentoBox;

    private JTextField cercaField;
    private JComboBox<String> cercaTipo;

    public LibreriaGUI() {
        setTitle("Gestione Libreria Personale");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        caricaLibreria();

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



        aggiungiB.addActionListener(e -> aggiungiLibro());
        //aggiornaB.addActionListener(e -> modificaLibro());
        //eliminaB.addActionListener(e -> rimuoviLibro());
        indietroB.addActionListener(e -> GestoreComandi.annullaUltimoComando());
        salvaB.addActionListener(e -> salvaLibreria());
        caricaB.addActionListener(e -> caricaLibreria());
        cercaB.addActionListener(e -> cercaLibro());


        filtroField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { aggiornaVista(); }
            public void removeUpdate(DocumentEvent e) { aggiornaVista(); }
            public void insertUpdate(DocumentEvent e) { aggiornaVista(); }
        });
        filtroBox.addActionListener(e -> aggiornaVista());
        ordinamentoBox.addActionListener(e -> aggiornaVista());


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
                    l.getStatoLettura(),
                    "Opzioni"
            });
        }
    }//AggiornaTabella


    private void aggiornaVista() {
        List<Libro> libri = Libreria.getInstance().getLibri();

        String filtroTesto = filtroField.getText().trim();
        String filtroTipo = (String) filtroBox.getSelectedItem();

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
        String ordinamentoTipo = (String) ordinamentoBox.getSelectedItem();
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



    void modificaLibro(int riga) {
        if (riga == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da modificare.");
            return;
        }


        try{
        String isbn = (String) tabellaModel.getValueAt(riga, 2);
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


    void rimuoviLibro(int riga) {
        if (riga == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere.");
            return;
        }

        String isbn = (String) tabellaModel.getValueAt(riga, 2);
        GestoreComandi.eseguiComando(new RimuoviLibroCMD(isbn));
    }//rimuoviLibro


    private void salvaLibreria() {
        FileLibreriaRepository.getInstance().salva(Libreria.getInstance().getLibri());

    }//salvaLibreria


    private void caricaLibreria() {
        List<Libro> libriCaricati = FileLibreriaRepository.getInstance().carica();
        Libreria.getInstance().setLibri(libriCaricati);

    }//caricaLibreria



    private void cercaLibro() {
        String testo = cercaField.getText().trim();

        List<Libro> libri = Libreria.getInstance().getLibri();

        if (!testo.isEmpty()) {
            RicercaStrategy strategy = new RicercaLibera();

            if (strategy != null) {
                List<Libro> risultati = libri.stream()
                        .filter(libro -> strategy.ricercaOK(libro, testo))
                        .collect(Collectors.toList());
                aggiornaTabella(risultati);
            }
        } else {
            aggiornaTabella(libri);
        }
    }//cercaLibro


}//LibreriaGUI
