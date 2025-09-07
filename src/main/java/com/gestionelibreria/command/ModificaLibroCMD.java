package com.gestionelibreria.command;

import com.gestionelibreria.libreria.Libreria;
import com.gestionelibreria.model.Libro;

public class ModificaLibroCMD implements Comando{

    private final Libro libroNuovo;
    private Libro libroVecchio;


    public ModificaLibroCMD(Libro libroNuovo) {

        this.libroNuovo = libroNuovo;
    }


    @Override
    public void esegui() {
        for (Libro l : Libreria.getInstance().getLibri()) {
            if (l.getIsbn().equals(libroNuovo.getIsbn())) {
                libroVecchio = l;
                break;
            }
        }
        Libreria.getInstance().modificaLibro(libroNuovo);
    }//esegui



    @Override
    public void annulla() {
        if (libroVecchio != null) {
            Libreria.getInstance().modificaLibro(libroVecchio);
        }
    }//annulla




}//ModificaLibroCMD
