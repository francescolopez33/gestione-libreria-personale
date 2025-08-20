package com.gestionelibreria.model;

import java.util.Objects;

public class Libro {

    private final String titolo;
    private final String autore;
    private final String isbn;
    private final String genere;
    private final int valutazione; // 1-5
    private final StatoLettura statoLettura;


    private Libro(Builder builder) {
        this.titolo = builder.titolo;
        this.autore = builder.autore;
        this.isbn = builder.isbn;
        this.genere = builder.genere;
        this.valutazione = builder.valutazione;
        this.statoLettura = builder.statoLettura;
    }

    //
    public String getTitolo() { return titolo; }
    public String getAutore() { return autore; }
    public String getIsbn() { return isbn; }
    public String getGenere() { return genere; }
    public int getValutazione() { return valutazione; }
    public StatoLettura getStatoLettura() { return statoLettura; }

    @Override
    public String toString() {
        return "Libro{" +
                "titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genere='" + genere + '\'' +
                ", valutazione=" + valutazione +
                ", statoLettura=" + statoLettura +
                '}';
    }//tostring

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return isbn.equals(libro.isbn); // confronto basato sull'ISBN
    }//equals

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }//hash


    public static class Builder {
        private String titolo;
        private String autore;
        private String isbn;
        private String genere;
        private int valutazione = 0; // default: 0 = non valutato
        private StatoLettura statoLettura = StatoLettura.DA_LEGGERE; // default

        public Builder titolo(String titolo) {
            this.titolo = titolo;
            return this;
        }

        public Builder autore(String autore) {
            this.autore = autore;
            return this;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder genere(String genere) {
            this.genere = genere;
            return this;
        }

        public Builder valutazione(int valutazione) {
            this.valutazione = valutazione;
            return this;
        }

        public Builder stato(StatoLettura statoLettura) {
            this.statoLettura = statoLettura;
            return this;
        }

        public Libro build() {
            //Controlli
            if (titolo == null || titolo.isBlank()) {
                throw new IllegalArgumentException("Il titolo non può essere vuoto");
            }
            if (autore == null || autore.isBlank()) {
                throw new IllegalArgumentException("L'autore non può essere vuoto");
            }
            if (isbn == null || isbn.isBlank()) {
                throw new IllegalArgumentException("L'ISBN non può essere vuoto");
            }
            if (valutazione < 0 || valutazione > 5) {
                throw new IllegalArgumentException("La valutazione deve essere tra 1 e 5");
            }
            return new Libro(this);
        }
    }//builder


}//libro
