package com.babelgroup.helloworld.loteriaLogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;
import java.util.*;

@Component
public class Runner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(LoteriaLoggingApplication.class);
    @Override
    public void run(String... args) throws Exception {

        try{
            boolean salir = false;

            while (!salir) {
                mostrarMenu();
                Scanner scanner = new Scanner(System.in);
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        insertarJugador(scanner);
                        break;
                    case 2:
                        insertarApuesta(scanner);
                        break;

                    case 3:
                        mostrarApuestas();
                        break;
                    case 4:
                        realizarSorteo();
                        break;
                    case 5:
                        salir = true;
                        break;
                    default:
                        logger.warn("Opción no válida del menu.");
                }
            }
        }catch (Exception e){
            logger.error("Valor introducido no numérico.");
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

    public static void insertarJugador(Scanner scanner) {
        logger.debug("Se ha seleccionado añadir un jugador.");
        boolean jugadorValido = false;
        while (!jugadorValido) {
            System.out.println("Introduce el nombre o 'x' para salir. ");
            String nombre = scanner.nextLine().toLowerCase().trim();
            if (nombre.equals("x")) {
                logger.info("El usuario ha vuelto al menu.");
                break;
            }
            if (JugadoresRepositorio.INSTANCE.comprobarJugadorRepetido(nombre)) {
                logger.warn("El jugador introducido ya existe.");
            } else {
                Jugador jugador = new Jugador(nombre);
                JugadoresRepositorio.INSTANCE.addJugador(jugador);
                jugadorValido = true;
                logger.info("Se ha creado un nuevo jugador.");
            }

        }
    }

    public static void insertarApuesta(Scanner scanner) {
        logger.debug("Se ha seleccionado añadir una apuesta.");
        Boolean existeJugador = false;
        while (!existeJugador) {
            System.out.println("¿Que jugador eres? Introduce tu nombre o pulse 'x' para salir.");
            String n = scanner.nextLine().toLowerCase().trim();
            if (n.equals("x")) {
                logger.info("El usuario ha vuelto al menu.");
                break;
            }

            Jugador jugador1 = JugadoresRepositorio.INSTANCE.getJugador(n);


            if (jugador1 != null) {
                logger.info("El jugador insertado existe.");
                existeJugador = true;
                Boolean pedirApuestas = true;
                Boolean crearApuesta = true;
                while (pedirApuestas) {
                    System.out.println("Introducir manualmente apuesta o aleatoria: [m/a] ");
                    String apuesta = scanner.nextLine().toLowerCase();

                    Set<Integer> conjunto = new HashSet<>();

                    if (apuesta.equals("m")) {
                        logger.info("El usuario quiere introducir la apuesta de forma manual.");
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
                        logger.info("El usuario quiere introducir la apuesta de forma aleatoria.");
                        Random random = new Random();
                        while (conjunto.size() < 6) {
                            int numero = random.nextInt(49) + 1;
                            conjunto.add(numero);
                        }
                    }else{
                        logger.error("Opcion de apuesta no valida. ");
                        crearApuesta = false;
                    }
                   if(crearApuesta) {
                       ArrayList<Integer> numerosApuesta = new ArrayList<>(conjunto);
                       Apuesta apuesta1 = new Apuesta(numerosApuesta);

                       if (jugador1.comprobarApuestaRepetida(apuesta1)) {
                           System.out.println("La apuesta introducida ya existe.");
                       } else {
                           pedirApuestas = false;
                           jugador1.addApuesta(apuesta1);
                       }
                   }
                }
            } else {
                logger.warn("El jugador insertado no existe.");
            }
        }
    }
    public static void mostrarApuestas() {
        logger.debug("El usuario ha entrado en mostrar apuestas.");
        Map<String, Jugador> jugadores = JugadoresRepositorio.INSTANCE.getAllJugadores();
        for (Jugador jugadorPrint : jugadores.values()) {
            System.out.println("Apuestas de " + jugadorPrint.getNombre() + ":");

            for (Apuesta apuesta : jugadorPrint.getApuestas()) {
                System.out.println(apuesta.numerosApuesta());
            }
        }
    }
    public static void realizarSorteo() {
        logger.debug("El usuario ha entrado en realizar sorteo.");
        Set<Integer> ganador = new HashSet<>();
        Random random = new Random();
        while (ganador.size() < 6) {
            int numero = random.nextInt(49) + 1;
            ganador.add(numero);
        }
        ArrayList<Integer> numerosGanador = new ArrayList<>(ganador);
        Apuesta premio = new Apuesta(numerosGanador);
        Map<String, Jugador> ganadores = JugadoresRepositorio.INSTANCE.getAllJugadores();
        logger.debug("El numero ganador ha sido: "+premio.numerosApuesta());
        for (Jugador jugadorPrint : ganadores.values()) {
            if (jugadorPrint.comprobarApuestaRepetida(premio)) {
                logger.info(jugadorPrint+ " ha ganado la loteria!!!!");
            }
        }
    }

}

