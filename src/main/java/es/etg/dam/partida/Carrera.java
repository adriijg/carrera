package es.etg.dam.partida;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.etg.dam.cliente.Cliente;
import es.etg.dam.common.Conexion;

public class Carrera implements Runnable {

    private static final int MAX_PUNTOS = 100;
    private static final int MAX_AVANCE = 10;
    private static final int TIEMPO = 2000;
    private static final int TIEMPO_TURNOS = 300;
    private static final int UNO = 1;
    private static final int CERO = 0;
    private static final String FORMATO = "%s%s: %d | ";
    private static final String ASTEDISCO = "*";

    private final List<Jugador> jugadores;

    public Carrera() {
        jugadores = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TIEMPO);

            Jugador ganador = null;

            while (ganador == null) {
                Jugador jugadorActual = avanzar();

                notificar(jugadorActual);

                if (jugadorActual.hasGanado()) {
                    ganador = jugadorActual;
                }
                Thread.sleep(TIEMPO_TURNOS);

            }
            finalizar(ganador);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Jugador avanzar() {
        Jugador jugador = jugadorAleatorio();
        int puntos = generarNumero(UNO, MAX_AVANCE);
        jugador.sumar(puntos);
        return jugador;
    }

    private int generarNumero(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + UNO) + min;
    }

    private Jugador jugadorAleatorio() {
        int posicion = generarNumero(CERO, jugadores.size() - UNO);
        return jugadores.get(posicion);
    }

    private void notificar(Jugador jugador) throws IOException {
        String estado = obtenerEstadoCarrera(jugador);
        for (Jugador jug : jugadores) {
            Conexion.enviar(estado, jug.getConexion());
        }
    }

    private String obtenerEstadoCarrera(Jugador jugador) {
        String estado = "";

        for (Jugador jug : jugadores) {
            String marca = (jug == jugador) ? ASTEDISCO : "";
            estado = String.format(FORMATO, estado, jug.getNombre() + marca, jug.getPuntos());
        }

        return estado;
    }

    private void finalizar(Jugador ganador) throws IOException {
        for (Jugador jugador : jugadores) {
            String mensaje = (jugador == ganador) ? Cliente.MSG_GANADO : Cliente.MSG_PERDIDO;
            Conexion.enviar(mensaje, jugador.getConexion());
            jugador.getConexion().close();

        }
    }

    public void registrarJugadores(String nombre, Socket socket) {
        jugadores.add(new Jugador(nombre, socket));
    }

}
