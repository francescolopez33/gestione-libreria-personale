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

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class LibreriaMediator {

    private final LibreriaGUI gui;

    public LibreriaMediator(LibreriaGUI gui) {
        this.gui = gui;
    }


    public void aggiornaVista() {
        List<Libro> libri = Libreria.getInstance().getLibri();

        String filtroTesto = gui.filtroField.getText().trim();
        String filtroTipo = (String) gui.filtroBox.getSelectedItem();

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
        String ordinamentoTipo = (String) gui.ordinamentoBox.getSelectedItem();
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



    private void aggiornaTabella(List<Libro> libri) {
        gui.tabellaModel.setRowCount(0);
        for (Libro l : libri) {
            gui.tabellaModel.addRow(new Object[]{
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



    public void onAggiungiClicked() {
        try {
            String titolo = JOptionPane.showInputDialog(gui, "Titolo:");
            String autore = JOptionPane.showInputDialog(gui, "Autore:");
            String isbn = JOptionPane.showInputDialog(gui, "ISBN:");
            String genere = JOptionPane.showInputDialog(gui, "Genere:");
            int valutazione = Integer.parseInt(JOptionPane.showInputDialog(gui, "Valutazione (1-5):"));
            StatoLettura stato = StatoLettura.valueOf(
                    JOptionPane.showInputDialog(gui, "Stato (LETTO, IN_LETTURA, DA_LEGGERE):")
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
            JOptionPane.showMessageDialog(gui, "Errore di input. Controlla i valori inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }//

    public void onModificaClicked(int riga) {
        if (riga == -1) {
            JOptionPane.showMessageDialog(gui, "Seleziona un libro da modificare.");
            return;
        }

        try {
            String isbn = (String) gui.tabellaModel.getValueAt(riga, 2);
            String nuovoTitolo = JOptionPane.showInputDialog(gui, "Nuovo titolo:");
            String nuovoAutore = JOptionPane.showInputDialog(gui, "Nuovo autore:");
            String nuovoGenere = JOptionPane.showInputDialog(gui, "Nuovo genere:");
            int nuovaValutazione = Integer.parseInt(JOptionPane.showInputDialog(gui, "Nuova valutazione (1-5):"));
            StatoLettura nuovoStato = StatoLettura.valueOf(
                    JOptionPane.showInputDialog(gui, "Nuovo stato (LETTO, IN_LETTURA, DA_LEGGERE):")
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
            JOptionPane.showMessageDialog(gui, "Errore di input. Controlla i valori inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }//onModificaClick


    public void onRimuoviClicked(int riga) {
        if (riga == -1) {
            JOptionPane.showMessageDialog(gui, "Seleziona un libro da rimuovere.");
            return;
        }

        String isbn = (String) gui.tabellaModel.getValueAt(riga, 2);
        GestoreComandi.eseguiComando(new RimuoviLibroCMD(isbn));
    }//rimuovi

    public void onIndietroClicked() {
        GestoreComandi.annullaUltimoComando();
    }//indietro

    public void onSalvaClicked() {
        FileLibreriaRepository.getInstance().salva(Libreria.getInstance().getLibri());
    }//salva

    public void onCaricaClicked() {
        caricaLibreria();
    }//carica

    public void caricaLibreria() {
        List<Libro> libriCaricati = FileLibreriaRepository.getInstance().carica();
        Libreria.getInstance().setLibri(libriCaricati);
    }//carica

    public void onCercaClicked() {
        String testo = gui.cercaField.getText().trim();
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

    public void onFiltroOrdinamentoChanged() {
        aggiornaVista();
    }//filtroOrdin


}//LibreriaMediator
