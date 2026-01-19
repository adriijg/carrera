package es.etg.dam.partida;

import java.util.Random;

public class Carrera implements Runnable {

    private final Random random = new Random();

    private Jugador[] Jugador;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    private Jugador avanzar() {
        int jug = random.nextInt();
    }

}
