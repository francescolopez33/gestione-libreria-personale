package com.gestionelibreria.persistenza;

import com.gestionelibreria.model.Libro;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileLibreriaRepository {

    private static FileLibreriaRepository instance;
    private static final String FILE_PATH = "libreria.json";
    private final Gson gson;


    //(Singleton)
    private FileLibreriaRepository() {
        this.gson = new Gson();
    }



    //Metodo per l'unica istanza
    public static synchronized FileLibreriaRepository getInstance() {
        if (instance == null) {
            instance = new FileLibreriaRepository();
        }
        return instance;
    }




    //Salva la lista dei libri sul file JSON e sovrascrive il contenuto precedente.

    public void salva(List<Libro> libri) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(libri, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//salva



     //Carica la lista dei libri da file JSON e se il file non esiste o è vuoto, restituisce una lista vuota.

    public List<Libro> carica() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Libro>>() {}.getType();
            List<Libro> libri = gson.fromJson(reader, listType);
            return libri != null ? libri : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }//carica




}//filelibreriarepository
