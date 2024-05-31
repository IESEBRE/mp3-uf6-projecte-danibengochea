package org.example.model.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;

public class F1pojo implements Serializable {
    private static final long serialVersionUID = -1932954327115814263L;

    private String pilot;
    private Double altura;
    private boolean campeoMundial;
    private int numVictories;
    private String escuderies;
    private int Numero;

    public F1pojo(String pilot, Double altura, boolean campeoMundial, int numVictories, String escuderies, int Numero) {
        this.pilot = pilot;
        this.altura = altura;
        this.campeoMundial = campeoMundial;
        this.numVictories = numVictories;
        this.escuderies = escuderies;
        this.Numero = Numero;
    }

    public F1pojo(String pilot, Double altura, boolean campeoMundial, int numVictories, int Numero) {
        this.pilot = pilot;
        this.altura = altura;
        this.campeoMundial = campeoMundial;
        this.numVictories = numVictories;
    }

    public String getPilot() {
        return pilot;
    }

    public void setPilot(String pilot) {
        this.pilot = pilot;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public boolean isCampeoMundial() {
        return campeoMundial;
    }

    public void setCampeoMundial(boolean campeoMundial) {
        this.campeoMundial = campeoMundial;
    }

    public int getNumVictories() {
        return numVictories;
    }

    public void setNumVictories(int numVictories) {
        this.numVictories = numVictories;
    }

    public String getEscuderies() {
        return escuderies;
    }

    public int getNumero() {
        return Numero;
    }

    @Override
    public String toString() {
        return "F1 {" + "Pilot: " + pilot + ", Altura: " + altura + "m, Campió Mundial: " + campeoMundial + ", Victòries: " + numVictories + "}";
    }

    public static class Equips {
        public enum Escuderia {
            MERCEDES("Mercedes"), REDBULL("Red Bull"), FERRARI("Ferrari"), ALPINE("Alpine"), ALPHATAURI("AlphaTauri"),
            ASTONMARTIN("Aston Martin"), WILLIAMS("Williams"), HAAS("Haas"), ALFAROMEO("Alfa Romeo"), MCLAREN("McLaren");

            private String escuderia;

            Escuderia(String escuderia) {
                this.escuderia = escuderia;
            }

            public String getEscuderia() {
                return escuderia;
            }

            @Override
            public String toString() {
                return  escuderia;
            }
        }
    }
}
