package com.gestionelibreria.libreria;

import com.gestionelibreria.model.Libro;
import com.gestionelibreria.observer.Observer;
import com.gestionelibreria.strategy.OrdinamentoStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Libreria {

    //singleton
    private static Libreria instance;

    private final List<Libro> libri;

    //Observers
    private final List<Observer> observers;


    private Libreria() {
        this.libri = new ArrayList<>();
        this.observers = new ArrayList<>();
    }//costruttore


    public static synchronized Libreria getInstance() {
        if (instance == null) {
            instance = new Libreria();
        }
        return instance;
    }//getInstance



    public boolean aggiungiLibro(Libro libro) {
        //Controllo dell'ISBN
        for (Libro l : libri) {
            if (l.getIsbn().equals(libro.getIsbn())) {
                return false;
            }
        }
        boolean successo = libri.add(libro);
        if (successo) {
            notifyObservers();
        }
        return successo;
    }//aggiungiLibro



    public boolean rimuoviLibro(String isbn) {
        boolean successo = libri.removeIf(l -> l.getIsbn().equals(isbn));
        if (successo) {
            notifyObservers();
        }
        return successo;
    }//rimuoviLibro


    public boolean modificaLibro(Libro libro) {
        for (int i = 0; i < libri.size(); i++) {
            if (libri.get(i).getIsbn().equals(libro.getIsbn())) {
                libri.set(i, libro);
                notifyObservers();
                return true;
            }
        }
        return false;
    }//modificaLibro



    public List<Libro> getLibri() {
        return Collections.unmodifiableList(libri);
    }//getLibri



    public void svuotaLibreria() {
        libri.clear();
    }//svuota


    public void registraObserver(Observer observer) {
        observers.add(observer);
    }//registraObserver

    public void rimuoviObserver(Observer observer) {
        observers.remove(observer);
    }//rimuoviObserver

    private void notifyObservers() {
        for (Observer o : observers) {
            o.update(Collections.unmodifiableList(libri));
        }
    }//notificaObserver


    public List<Libro> getLibriOrdinati(OrdinamentoStrategy strategy) {
        return strategy.ordina(new ArrayList<>(libri));
    }//getLibriOrdinati


    public void setLibri(List<Libro> nuovaListaDiLibri) {
        this.libri.clear();
        this.libri.addAll(nuovaListaDiLibri);
        notifyObservers();
    }//setLibri

}//libreria
