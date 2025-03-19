package com.babelgroup.helloworld.loteriaLogging;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.util.*;

@Component
public class Runner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            Scanner scanner = new Scanner(System.in);
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    boolean jugadorValido = false;
                    while(!jugadorValido) {
                        System.out.println("Introduce el nombre o 'x' para salir. ");
                        String nombre = scanner.nextLine().toLowerCase().trim();
                        if (nombre.equals("x")) {
                            break;
                        }
                        if (JugadoresRepositorio.INSTANCE.comprobarJugadorRepetido(nombre)) {
                            System.out.println("El jugador introducido ya existe.");
                        }else{
                            Jugador jugador = new Jugador(nombre);
                            JugadoresRepositorio.INSTANCE.addJugador(jugador);
                            jugadorValido = true;
                        }

                    }
                    break;

                case 2:
                    Boolean existeJugador = false;
                    while(!existeJugador) {
                        System.out.println("¿Que jugador eres? Introduce tu nombre o pulse 'x' para salir.");
                        String n = scanner.nextLine().toLowerCase().trim();
                        if(n.equals("x")){
                            break;
                        }

                        Jugador jugador1 = JugadoresRepositorio.INSTANCE.getJugador(n);


                        if (jugador1 != null) {
                            existeJugador = true;
                            Boolean pedirApuestas = true;
                            while (pedirApuestas) {
                                System.out.println("Introducir manualmente apuesta o aleatoria: [m/a] ");
                                String apuesta = scanner.nextLine().toLowerCase();

                                Set<Integer> conjunto = new HashSet<>();

                                if (apuesta.equals("m")) {
                                    int cont = 0;
                                    while (conjunto.size() < 6) {
                                        System.out.println("Introducir numero de la apuesta: ");
                                        int num = scanner.nextInt();
                                        scanner.nextLine();
                                        conjunto.add(num);
                                        cont++;
                                        if (cont > conjunto.size()) {
                                            System.out.println("No se pueden introducir dos numeros iguales.");
                                            cont = conjunto.size();
                                        }
                                    }
                                } else if (apuesta.equals("a")) {
                                    Random random = new Random();
                                    while (conjunto.size() < 6) {
                                        int numero = random.nextInt(49) + 1;
                                        conjunto.add(numero);
                                    }
                                }
                                ArrayList<Integer> numerosApuesta = new ArrayList<>(conjunto);
                                Apuesta apuesta1 = new Apuesta(numerosApuesta);

                                if (jugador1.comprobarApuestaRepetida(apuesta1)) {
                                    System.out.println("La apuesta introducida ya existe.");
                                } else {
                                    pedirApuestas = false;
                                    jugador1.addApuesta(apuesta1);
                                }
                            }
                        }else{
                            System.out.println("El jugador no existe. ");
                        }
                    }
                    break;

                case 3:
                    Map<String, Jugador> jugadores = JugadoresRepositorio.INSTANCE.getAllJugadores();
                    for (Jugador jugadorPrint : jugadores.values()) {
                        System.out.println("Apuestas de " + jugadorPrint.getNombre() + ":");

                        for (Apuesta apuesta : jugadorPrint.getApuestas()) {
                            System.out.println(apuesta.numerosApuesta());
                        }
                    }
                    break;
                case 4:
                    //Realizar sorteo
                    Set<Integer> ganador = new HashSet<>();
                    Random random = new Random();
                    while (ganador.size() < 6) {
                        int numero = random.nextInt(49) + 1;
                        ganador.add(numero);
                    }
                    ArrayList<Integer> numerosGanador = new ArrayList<>(ganador);
                    Apuesta premio = new Apuesta(numerosGanador);
                    Map<String, Jugador> ganadores = JugadoresRepositorio.INSTANCE.getAllJugadores();
                    System.out.println("El numero premiado es: "+premio.numerosApuesta());
                    for (Jugador jugadorPrint : ganadores.values()) {
                        if (jugadorPrint.comprobarApuestaRepetida(premio)) {
                            System.out.println(jugadorPrint+ " ha ganado la loteria!!!!");
                        }
                    }

                    break;
                case 5:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    public static void mostrarMenu() {
        System.out.println("\n--- Menú Lotería Primitiva ---");
        System.out.println("1. Añadir Jugador");
        System.out.println("2. Añadir Apuesta");
        System.out.println("3. Mostrar Apuestas");
        System.out.println("4. Realizar Sorteo");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }
    }

