package com.gestionelibreria.view;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;
import com.gestionelibreria.model.StatoLettura;
import java.util.Scanner;

public class ConsoleUI {
    private final Libreria libreria;
    private final Scanner scanner;
    private boolean running;

    public ConsoleUI() {
        this.libreria = Libreria.getInstance();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    public void start() {
        System.out.println("=== GESTIONE LIBRERIA PERSONALE ===");

        while (running) {
            mostraMenu();
            int scelta = leggiScelta();
            elaboraScelta(scelta);
        }
    }//start

    private void mostraMenu() {
        System.out.println("\n--- MENU PRINCIPALE ---");
        System.out.println("1. Aggiungi nuovo libro");
        System.out.println("2. Visualizza tutti i libri");
        System.out.println("3. Cerca libri");
        System.out.println("4. Esci");
        System.out.print("Scegli un'opzione: ");
    }

    private int leggiScelta() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void elaboraScelta(int scelta) {
        switch (scelta) {
            case 1 -> aggiungiLibro();
            case 2 -> visualizzaLibri();
            case 3 -> cercaLibri();
            case 4 -> uscita();
            default -> System.out.println("Opzione non valida!");
        }
    }

    private void aggiungiLibro() {
        System.out.println("\n--- AGGIUNGI NUOVO LIBRO ---");

        try {
            Libro libro = new Libro.Builder()
                    .titolo(leggiInput("Titolo: "))
                    .autore(leggiInput("Autore: "))
                    .isbn(leggiInput("ISBN: "))
                    .genere(leggiInput("Genere: "))
                    .valutazione(Integer.parseInt(leggiInput("Valutazione (1-5): ")))
                    .stato(leggiStatoLettura())
                    .build();

            if (libreria.aggiungiLibro(libro)) {
                System.out.println("Libro aggiunto con successo!");
            } else {
                System.out.println("Errore: ISBN già esistente!");
            }

        } catch (Exception e) {
            System.out.println("Errore nella creazione del libro: " + e.getMessage());
        }
    }

    private String leggiInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private StatoLettura leggiStatoLettura() {
        System.out.println("Stato lettura:");
        System.out.println("1. Letto");
        System.out.println("2. In lettura");
        System.out.println("3. Da leggere");
        System.out.print("Scegli (1-3): ");

        int scelta = leggiScelta();
        return switch (scelta) {
            case 1 -> StatoLettura.LETTO;
            case 2 -> StatoLettura.IN_LETTURA;
            case 3 -> StatoLettura.DA_LEGGERE;
            default -> StatoLettura.DA_LEGGERE;
        };
    }

    private void visualizzaLibri() {
        System.out.println("\n--- TUTTI I LIBRI (" + libreria.getLibri().size() + ") ---");

        if (libreria.getLibri().isEmpty()) {
            System.out.println("Nessun libro in libreria.");
            return;
        }

        int index = 1;
        for (Libro libro : libreria.getLibri()) {
            System.out.println("\n" + index + ". " + libro);
            index++;
        }
    }

    private void cercaLibri() {
        System.out.println("\n--- CERCA LIBRI ---");
        System.out.println("Funzionalità di ricerca sarà implementata presto!");
        // Per ora placeholder, implementerai dopo
    }

    private void uscita() {
        System.out.println("Arrivederci!");
        running = false;
        scanner.close();
    }
}