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
import java.util.stream.Collectors;

public class LibreriaMediator {

    private final LibreriaGUI gui;

    public LibreriaMediator(LibreriaGUI gui) {
        this.gui = gui;
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

        //Ordinamento
        OrdinamentoStrategy strategy = null;
        String ordinamentoTipo = (String) gui.getOrdinamentoBox().getSelectedItem();
        switch (ordinamentoTipo) {
            case "Titolo":
                strategy = new OrdinamentoPerTitolo(); break;
            case "Autore":
                strategy = new OrdinamentoPerAutore(); break;
            case "Valutazione":
                strategy = new OrdinamentoPerValutazione(); break;
            case "Stato Lettura":
                strategy = new OrdinamentoPerStatoLettura(); break;
        }
        if (strategy != null) {
            libri = strategy.ordina(libri);
        }

        aggiornaTabella(libri);
    }//aggiornaVisita



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
        try {
            String titolo = JOptionPane.showInputDialog(gui, "Titolo:");
            String autore = JOptionPane.showInputDialog(gui, "Autore:");
            String isbn = JOptionPane.showInputDialog(gui, "ISBN:");
            String genere = JOptionPane.showInputDialog(gui, "Genere:");
            int valutazione = Integer.parseInt(JOptionPane.showInputDialog(gui, "Valutazione (1-5):"));
            JComboBox<StatoLettura> stato = new JComboBox<>(StatoLettura.values());
            int res = JOptionPane.showConfirmDialog(gui, stato, "Seleziona Stato Lettura", JOptionPane.OK_CANCEL_OPTION);

            if (res != JOptionPane.OK_OPTION) {
                return;
            }
            StatoLettura statoFin = (StatoLettura) stato.getSelectedItem();

            Libro libro = new Libro.Builder()
                    .titolo(titolo)
                    .autore(autore)
                    .isbn(isbn)
                    .genere(genere)
                    .valutazione(valutazione)
                    .stato(statoFin)
                    .build();

            GestoreComandi.eseguiComando(new AggiungiLibroCMD(libro));
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(gui, "Errore di input. Controlla i valori inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }//aggiuntaLibro

    public void modificaLibroSelezionato(int riga) {
        if (riga == -1) {
            JOptionPane.showMessageDialog(gui, "Seleziona un libro da modificare.");
            return;
        }

        try {
            String isbn = (String) gui.getTabellaModel().getValueAt(riga, 2);
            String nuovoTitolo = JOptionPane.showInputDialog(gui, "Nuovo titolo:");
            String nuovoAutore = JOptionPane.showInputDialog(gui, "Nuovo autore:");
            String nuovoGenere = JOptionPane.showInputDialog(gui, "Nuovo genere:");
            int nuovaValutazione = Integer.parseInt(JOptionPane.showInputDialog(gui, "Nuova valutazione (1-5):"));

            JComboBox<StatoLettura> statoCombo = new JComboBox<>(StatoLettura.values());
            int res = JOptionPane.showConfirmDialog(gui, statoCombo, "Seleziona Nuovo Stato", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) {
                return;
            }
            StatoLettura nuovoStato = (StatoLettura) statoCombo.getSelectedItem();


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
    }//cerca

    public void applicaFiltriOrdinamento() {
        aggiornaVista();
    }//filtroOrdin


}//LibreriaMediator
