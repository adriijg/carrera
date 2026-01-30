package es.etg.dam.partida;

import java.io.IOException;
import java.util.Random;

import es.etg.dam.cliente.Cliente;
import es.etg.dam.common.Conexion;

public class Carrera implements Runnable {
    private static final int MAX_PUNTOS = 100;
    private static final int MAX_AVANCE = 10;
    private static final int TIEMPO = 2000;
    private static final int UNO = 1;
    private static final int CERO = 0;
    private static final String FORMATO = "%s%s:%d|";

    private Jugador[] jugadores;

    public Carrera(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TIEMPO);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Jugador ganador = null;

        while (ganador == null) {
            Jugador jugadorActual = avanzar();

            try {
                notificar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (jugadorActual.getPuntos() >= MAX_PUNTOS) {
                ganador = jugadorActual;
            }
        }
        finalizar(ganador);
    }

    private Jugador avanzar() {
        Jugador jugador = jugadorAleatorio();
        int puntos = puntosAleatorios();
        jugador.sumar(puntos);
        return jugador;
    }

    private final Random random = new Random();

    private int generarNumero(int min, int max) {
        return random.nextInt(max - min + UNO) + min;
    }

    private Jugador jugadorAleatorio() {
        int posicion = generarNumero(CERO, jugadores.length - UNO);
        return jugadores[posicion];
    }

    private int puntosAleatorios() {
        return generarNumero(UNO, MAX_AVANCE);
    }

    private void notificar() throws IOException {
        String estado = obtenerEstadoCarrera();
        for (Jugador jugador : jugadores) {
            Conexion.enviar(estado, jugador.getConexion());
        }

    }

    private String obtenerEstadoCarrera() {
        String estado = "";

        for (Jugador jugador : jugadores) {
            estado = String.format(FORMATO, estado, jugador.getNombre(), jugador.getPuntos());
        }

        return estado;
    }

    private void finalizar(Jugador ganador) {
        for (Jugador jugador : jugadores) {
            try {
                String mensaje = (jugador == ganador) ? Cliente.MSG_GANADO : Cliente.MSG_PERDIDO;

                Conexion.enviar(mensaje, jugador.getConexion());
                jugador.getConexion().close();

            } catch (IOException e) {
                new RuntimeException(e);
            }
        }
    }

}
