package es.etg.dam.partida;

import java.io.IOException;
import java.net.ServerSocket;

public class Servidor {

    static final int PUERTO = 8888;
    public final static int NUM_JUG = 4;

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PUERTO)) {
        } catch (IOException e) {

        }
    }

}
