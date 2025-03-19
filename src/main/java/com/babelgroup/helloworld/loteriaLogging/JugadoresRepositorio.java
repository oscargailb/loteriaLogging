package com.babelgroup.helloworld.loteriaLogging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JugadoresRepositorio {

    public static final JugadoresRepositorio INSTANCE = new JugadoresRepositorio();

    private final Map<String, Jugador> jugadores = new HashMap<>();

    public void addJugador(Jugador jugador) {    jugadores.put(jugador.getNombre(), jugador);}
    public Jugador getJugador(String name) {    return jugadores.get(name);}
    public Map<String, Jugador> getAllJugadores() {    return Collections.unmodifiableMap(jugadores);}
    public boolean comprobarJugadorRepetido(String nombre) {
        return jugadores.containsKey(nombre);
    }
}
