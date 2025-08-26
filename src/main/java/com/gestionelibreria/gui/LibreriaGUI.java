package com.gestionelibreria.gui;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.mediator.LibreriaMediator;
import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;
import com.gestionelibreria.observer.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibreriaGUI extends JFrame implements Observer{

    private JTable tabella;
    private DefaultTableModel tabellaModel;


    private JComboBox<String> ordinamentoBox;

    private JTextField cercaField;

    private final LibreriaMediator mediator;

    private JTextField filtroGenere;
    private JComboBox<StatoLettura> filtroStato;
    private JTextField filtroValMin;
    private JTextField filtroValMax;


    public LibreriaGUI() {
        setTitle("Gestione Libreria Personale");
        setSize(900, 650);
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
        cercaPanel.setBorder(BorderFactory.createTitledBorder("Ricerca"));
        cercaField = new JTextField(20);
        JButton cercaB = new JButton("Cerca");
        cercaB.setPreferredSize(new Dimension(100, 30));
        cercaPanel.add(new JLabel("Testo: "));
        cercaPanel.add(cercaField);
        cercaPanel.add(cercaB);
        //topPanel.add(cercaPanel, BorderLayout.NORTH);


        //filtri e ordinamento
        JPanel ordinamentoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ordinamentoPanel.setBorder(BorderFactory.createTitledBorder("Ordinamento"));

        ordinamentoBox = new JComboBox<>(new String[]{
                "Nessuno", "Titolo", "Autore", "Valutazione", "Stato Lettura"
        });
        ordinamentoPanel.add(new JLabel("Ordina per:"));
        ordinamentoPanel.add(ordinamentoBox);


        JPanel ricercaOrdinamentoPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        ricercaOrdinamentoPanel.add(cercaPanel);
        ricercaOrdinamentoPanel.add(ordinamentoPanel);
        topPanel.add(ricercaOrdinamentoPanel, BorderLayout.NORTH);


        //Pannello per i filtri
        JPanel filtroPanel = new JPanel(new GridLayout(2, 2, 15, 8));
        filtroPanel.setBorder(BorderFactory.createTitledBorder("Filtri"));

        filtroGenere = new JTextField(10);
        filtroStato = new JComboBox<>();
        filtroStato.addItem(null);
        filtroStato.addItem(StatoLettura.LETTO);
        filtroStato.addItem(StatoLettura.IN_LETTURA);
        filtroStato.addItem(StatoLettura.DA_LEGGERE);
        filtroValMin = new JTextField(3);
        filtroValMax = new JTextField(3);

        filtroPanel.add(new JLabel("Genere:"));
        filtroPanel.add(filtroGenere);
        filtroPanel.add(new JLabel("Stato:"));
        filtroPanel.add(filtroStato);
        filtroPanel.add(new JLabel("Val. Min:"));
        filtroPanel.add(filtroValMin);
        filtroPanel.add(new JLabel("Val. Max:"));
        filtroPanel.add(filtroValMax);

        topPanel.add(filtroPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);


        //Bottoni sotto sinistra
        JPanel bottone = new JPanel(new BorderLayout());
        JPanel pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton aggiungiB = new JButton("Aggiungi");
        JButton indietroB = new JButton("Indietro");
        JButton salvaB = new JButton("Salva");
        JButton caricaB = new JButton("Carica");

        //Font
        Font fontBottoni = new Font("Arial", Font.BOLD, 14);
        Dimension dimensioneGrande = new Dimension(130, 40);
        JButton[] bottoni = {aggiungiB, indietroB, salvaB, caricaB};
        for (JButton b : bottoni) {
            b.setFont(fontBottoni);
            b.setPreferredSize(dimensioneGrande);
        }

        pannelloBottoni.add(aggiungiB);
        pannelloBottoni.add(indietroB);
        filePanel.add(salvaB);
        filePanel.add(caricaB);

        bottone.add(pannelloBottoni, BorderLayout.WEST);
        bottone.add(filePanel, BorderLayout.EAST);
        add(bottone, BorderLayout.SOUTH);



        aggiungiB.addActionListener(e -> mediator.aggiuntaLibro());
        indietroB.addActionListener(e -> mediator.annullaUltima());
        salvaB.addActionListener(e -> mediator.salvaLibreria());
        caricaB.addActionListener(e -> mediator.caricaLibreriaDaFile());
        cercaB.addActionListener(e -> mediator.cercaLibri());
        cercaField.addActionListener(e -> mediator.cercaLibri());
        ordinamentoBox.addActionListener(e -> mediator.applicaFiltriOrdinamento());



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
        mediator.modificaLibroSelezionato(riga);
    }//modificaLibro


    void rimuoviLibro(int riga) {
        mediator.rimuoviLibroSelezionato(riga);
    }//rimuoviLibro


    //Metodi getter per accedere ai componenti privati
    public DefaultTableModel getTabellaModel() { return tabellaModel; }
    public JComboBox<String> getOrdinamentoBox() { return ordinamentoBox; }
    public JTextField getCercaField() { return cercaField; }
    public JTextField getFiltroGenere() { return filtroGenere; }
    public JComboBox<StatoLettura> getFiltroStato() { return filtroStato; }
    public JTextField getFiltroValMin() { return filtroValMin; }
    public JTextField getFiltroValMax() { return filtroValMax; }

}//LibreriaGUI
