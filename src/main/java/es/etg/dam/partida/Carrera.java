package es.etg.dam.partida;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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

    private final ArrayList<Jugador> jugadores;

    public Carrera() {
        jugadores = new ArrayList<>();
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
            try {
                Thread.sleep(TIEMPO_TURNOS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        finalizar(ganador);
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

    private void notificar() throws IOException {
        String estado = obtenerEstadoCarrera();
        Conexion.enviar(estado, jugadores.get(0).getConexion());
    }

    private String obtenerEstadoCarrera() {
        String estado = "";

        for (Jugador jugador : jugadores) {
            estado = String.format(FORMATO, estado, jugador.getNombre(), jugador.getPuntos());
        }

        return estado;
    }

    // String mensaje = (jugador ==ganador) ? Cliente.MSG_GANADO :
    // Cliente.MSG_PERDIDO;

    private void finalizar(Jugador ganador) {
        for (Jugador jugador : jugadores) {
            String mensaje;
            try {
                if (jugador == ganador) {
                    mensaje = Cliente.MSG_GANADO;
                } else {
                    mensaje = Cliente.MSG_PERDIDO;
                }
                Conexion.enviar(mensaje, jugador.getConexion());
                jugador.getConexion().close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void registrarJugadores(String nombre, Socket socket) {
        jugadores.add(new Jugador(nombre, socket));
    }

}
