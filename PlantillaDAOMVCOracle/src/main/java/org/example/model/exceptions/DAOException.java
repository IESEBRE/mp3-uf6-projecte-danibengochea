package org.example.model.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DAOException extends Exception{

    private static TreeMap<Integer, String> codis;
    private final int codi;
    static {
        excepcions();
    }
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public DAOException(int si) {
        codi= si;
    }
    public int getCodi(){
        return codi;
    }
    public String retornaMissatge(){
        return codis.get(codi);
    }

    static void excepcions(){
        codis = new TreeMap<>();
        codis.put(1, "Falta omplir alguna dada!");
        codis.put(2, "L'alçada ha d'estar entre 1.50 i 2.10");
        codis.put(3, "Aquesta entrada ja existeix a la taula!");
        codis.put(4, "No és un número vàlid per a l'altura");
        codis.put(5, "No hi ha cap fila seleccionada!");
        codis.put(6, "No es un numero");
        codis.put(7, "El format no es correcte e.x: Fernando Alonso");
        codis.put(8, "Error en la connexió a la base de dades");
    }
}
