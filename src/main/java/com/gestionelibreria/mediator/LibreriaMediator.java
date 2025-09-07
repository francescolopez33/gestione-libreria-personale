package com.gestionelibreria.mediator;

import com.gestionelibreria.command.AggiungiLibroCMD;
import com.gestionelibreria.command.GestoreComandi;
import com.gestionelibreria.command.ModificaLibroCMD;
import com.gestionelibreria.command.RimuoviLibroCMD;
import com.gestionelibreria.filtri.*;
import com.gestionelibreria.gui.LibreriaGUI;
import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;

import com.gestionelibreria.persistenza.FileLibreriaRepository;
import com.gestionelibreria.strategy.*;

import javax.swing.*;

import java.util.List;

public class LibreriaMediator {

    private final LibreriaGUI gui;
    private final OrdinamentoContext ordinamentoContext;
    private final RicercaContext ricercaContext;

    public LibreriaMediator(LibreriaGUI gui) {
        this.gui = gui;
        this.ordinamentoContext = new OrdinamentoContext(new OrdinamentoPerTitolo());
        this.ricercaContext = new RicercaContext(new RicercaLibera());
    }


    public void aggiornaVista() {
        List<Libro> libri = Libreria.getInstance().getLibri();
        FiltroLibro filtro = new FiltroConcreto();

        String genere = gui.getFiltroGenere().getText().trim();
        if (!genere.isEmpty()) {
            filtro = new FiltroGenere(filtro, genere);
        }

        StatoLettura stato = (StatoLettura) gui.getFiltroStato().getSelectedItem();
        if (stato != null) {
            filtro = new FiltroStatoLettura(filtro, stato);
        }

        String valMinText = gui.getFiltroValMin().getText().trim();
        if (!valMinText.isEmpty()) {
            try {
                int valMin = Integer.parseInt(valMinText);
                filtro = new FiltroValutazioneMinima(filtro, valMin);
            } catch (NumberFormatException ignored) {}
        }

        String valMaxText = gui.getFiltroValMax().getText().trim();
        if (!valMaxText.isEmpty()) {
            try {
                int valMax = Integer.parseInt(valMaxText);
                filtro = new FiltroValutazioneMassima(filtro, valMax);
            } catch (NumberFormatException ignored) {}
        }

        libri = filtro.filtra(libri);


        String ordinamentoTipo = (String) gui.getOrdinamentoBox().getSelectedItem();
        OrdinamentoStrategy strategy = getOrdinamentoStrategy(ordinamentoTipo);
        ordinamentoContext.setStrategy(strategy);
        libri = ordinamentoContext.ordina(libri);

        aggiornaTabella(libri);
    }//aggiornaVisita

    private OrdinamentoStrategy getOrdinamentoStrategy(String tipo) {
        return switch (tipo) {
            case "Titolo" -> new OrdinamentoPerTitolo();
            case "Autore" -> new OrdinamentoPerAutore();
            case "Valutazione" -> new OrdinamentoPerValutazione();
            case "Stato Lettura" -> new OrdinamentoPerStatoLettura();
            default -> new OrdinamentoPerTitolo(); // Strategia di default
        };
    }



    private void aggiornaTabella(List<Libro> libri) {
        gui.getTabellaModel().setRowCount(0);
        for (Libro l : libri) {
            gui.getTabellaModel().addRow(new Object[]{
                    l.getTitolo(),
                    l.getAutore(),
                    l.getIsbn(),
                    l.getGenere(),
                    l.getValutazione(),
                    l.getStatoLettura(),
                    "Opzioni"
            });
        }
    }//aggiornaTabella



    public void aggiuntaLibro() {
        JPanel pannello = new JPanel();
        pannello.setLayout(new BoxLayout(pannello, BoxLayout.Y_AXIS));

        JTextField titoloField = new JTextField(20);
        JTextField autoreField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextField genereField = new JTextField(20);
        JComboBox<StatoLettura> statoCombo = new JComboBox<>(StatoLettura.values());
        JTextField valutazioneField = new JTextField(5);


        statoCombo.addActionListener(e -> {
            StatoLettura statoCorrente = (StatoLettura) statoCombo.getSelectedItem();
            boolean isLetto = statoCorrente == StatoLettura.LETTO;
            valutazioneField.setEnabled(isLetto);
            if (!isLetto) {
                valutazioneField.setText("0");
            }
        });


        pannello.add(new JLabel("Titolo:"));
        pannello.add(titoloField);
        pannello.add(new JLabel("Autore:"));
        pannello.add(autoreField);
        pannello.add(new JLabel("ISBN:"));
        pannello.add(isbnField);
        pannello.add(new JLabel("Genere:"));
        pannello.add(genereField);
        pannello.add(new JLabel("Stato di Lettura:"));
        pannello.add(statoCombo);
        pannello.add(new JLabel("Valutazione (1-5):"));
        pannello.add(valutazioneField);


        int res = JOptionPane.showConfirmDialog(gui, pannello, "Nuovo Libro", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String titolo = titoloField.getText().trim();
                String autore = autoreField.getText().trim();
                String isbn = isbnField.getText().trim();
                String genere = genereField.getText().trim();
                StatoLettura stato = (StatoLettura) statoCombo.getSelectedItem();
                int valutazione = 0;


                if (stato == StatoLettura.LETTO) {
                    valutazione = Integer.parseInt(valutazioneField.getText().trim());
                }

                Libro libro = new Libro.Builder()
                        .titolo(titolo)
                        .autore(autore)
                        .isbn(isbn)
                        .genere(genere)
                        .valutazione(valutazione)
                        .stato(stato)
                        .build();

                GestoreComandi.eseguiComando(new AggiungiLibroCMD(libro));


            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(gui, "Errore: Inserisci un numero valido per la valutazione.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                if (e.getMessage() != null && e.getMessage().contains("ISBN")) {
                    JOptionPane.showMessageDialog(gui, "Errore: Un libro con questo ISBN è già presente.", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(gui, "Errore di input. Controlla i valori inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//aggiuntaLibro


    public void modificaLibroSelezionato(int riga) {
        if (riga == -1) {
            JOptionPane.showMessageDialog(gui, "Seleziona un libro da modificare.");
            return;
        }

        try {

            String titoloEsistente = (String) gui.getTabellaModel().getValueAt(riga, 0);
            String autoreEsistente = (String) gui.getTabellaModel().getValueAt(riga, 1);
            String isbn = (String) gui.getTabellaModel().getValueAt(riga, 2);
            String genereEsistente = (String) gui.getTabellaModel().getValueAt(riga, 3);
            int valutazioneEsistente = (int) gui.getTabellaModel().getValueAt(riga, 4);
            StatoLettura statoEsistente = (StatoLettura) gui.getTabellaModel().getValueAt(riga, 5);



            String nuovaValutazioneStringa = JOptionPane.showInputDialog(gui, "Nuova valutazione (1-5):", valutazioneEsistente);

            if (nuovaValutazioneStringa == null) {
                return;
            }
            int nuovaValutazione = Integer.parseInt(nuovaValutazioneStringa);


            JComboBox<StatoLettura> statoCombo = new JComboBox<>(StatoLettura.values());
            statoCombo.setSelectedItem(statoEsistente);
            int res = JOptionPane.showConfirmDialog(gui, statoCombo, "Seleziona Nuovo Stato", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) {
                return;
            }
            StatoLettura nuovoStato = (StatoLettura) statoCombo.getSelectedItem();



            Libro libroModificato = new Libro.Builder()
                    .titolo(titoloEsistente)
                    .autore(autoreEsistente)
                    .isbn(isbn)
                    .genere(genereEsistente)
                    .valutazione(nuovaValutazione)
                    .stato(nuovoStato)
                    .build();


            GestoreComandi.eseguiComando(new ModificaLibroCMD(libroModificato));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(gui, "Errore di input. Controlla i valori inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }//modificaLibroSelezionato


    public void rimuoviLibroSelezionato(int riga) {
        if (riga == -1) {
            JOptionPane.showMessageDialog(gui, "Seleziona un libro da rimuovere.");
            return;
        }

        String isbn = (String) gui.getTabellaModel().getValueAt(riga, 2);
        GestoreComandi.eseguiComando(new RimuoviLibroCMD(isbn));
    }//rimuovi

    public void annullaUltima() {
        GestoreComandi.annullaUltimoComando();
    }//indietro

    public void salvaLibreria() {
        FileLibreriaRepository.getInstance().salva(Libreria.getInstance().getLibri());
    }//salva

    public void caricaLibreriaDaFile() {
        caricaLibreria();
    }//carica

    public void caricaLibreria() {
        List<Libro> libriCaricati = FileLibreriaRepository.getInstance().carica();
        Libreria.getInstance().setLibri(libriCaricati);
    }//carica

    public void cercaLibri() {
        String testo = gui.getCercaField().getText().trim();
        List<Libro> libri = Libreria.getInstance().getLibri();

        if (!testo.isEmpty()) {
            // Usa il contesto di ricerca per eseguire la ricerca
            ricercaContext.setStrategy(getRicercaStrategy());
            List<Libro> risultati = ricercaContext.ricerca(libri, testo);
            aggiornaTabella(risultati);
        } else {
            aggiornaTabella(libri);
        }
    }//cerca

    private RicercaStrategy getRicercaStrategy() {
        return new RicercaLibera();
    }//ricercaStrategy

    public void applicaFiltriOrdinamento() {
        aggiornaVista();
    }//filtroOrdin


}//LibreriaMediator
