package com.babelgroup.helloworld.loteriaLogging;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private List<Apuesta> apuestas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.apuestas = new ArrayList<>();
    }

    public void addApuesta(Apuesta apuesta) {
        apuestas.add(apuesta);
    }

    public List<Apuesta> getApuestas() {
        return apuestas;
    }
    public String getNombre() {
        return nombre;
    }

    public boolean comprobarApuestaRepetida(Apuesta apuesta) {
        return apuestas.contains(apuesta);
    }


}
