package es.etg.dam.partida;

import java.io.IOException;
import java.util.Random;

import es.etg.dam.conexion.Conexion;

public class Carrera implements Runnable {
    private final int NUM_RANGO = 10;
    private final int NUM_PUNTOS = 100;
    private final String MSG_GANADO = "ENHORABUENA";
    private final String MSG_PERDIDO = "GAME OVER";

    private final Random random = new Random();

    private Jugador[] jugadores;

    public Carrera(Jugador[] jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Jugador jugador = null;

        while (ganador == null) {
            Jugador j = avanzar();
        }

    }

    private Jugador avanzar() {
        int jug = random.nextInt(Servidor.NUM_JUG);
        int puntos = random.nextInt(NUM_RANGO + 1);
        jugadores[jug].sumar(puntos);
        return jugadores[jug];
    }

    private void notificar(Jugador jugador) throws IOException {
        String msg = obtenerPuntos();
        Conexion.enviar(msg, jugador.getConexion());

    }

    private String obtenerPuntos() throws IOException {
        StringBuilder sb = new StringBuilder();

        for (Jugador j : jugadores) {
            sb.append(j.getNombre());
            sb.append(":");
            sb.append(j.getPuntos());
            sb.append("|");
        }

        return sb.toString();
    }

    private void finalizar(Jugador jugador) {
        for (Jugador j : jugadores) {
            try {
                if (j == ganador) {
                    Conexion.enviar(MSG_GANADO, j.getConexion());
                } else {
                    Conexion.enviar(MSG_PERDIDO, j.getConexion());
                }
                j.getConexion().close();

            } catch (IOException e) {
            }
        }
    }

}
