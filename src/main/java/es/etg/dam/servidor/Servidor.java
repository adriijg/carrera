package es.etg.dam.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.etg.dam.cliente.Cliente;
import es.etg.dam.common.Conexion;
import es.etg.dam.partida.Carrera;
import es.etg.dam.partida.Jugador;

public class Servidor {

    public static final String HOST = "localhost";
    public static final int PUERTO = 8888;
    public final static int MAX_JUGADORES = 4;
    private final static String MSG_JUGADOR_REGISTRADO = "Jugador registrado: %s";

    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(PUERTO)) {

            while (true) {
                Jugador[] jugadores = new Jugador[MAX_JUGADORES];

                for (int i = 0; i < MAX_JUGADORES; i++) {
                    Socket socket = server.accept();
                    String nombre = Conexion.recibir(socket);
                    Conexion.enviar(Cliente.OK, socket);

                    jugadores[i] = new Jugador(nombre, socket);
                    System.out.println(String.format(MSG_JUGADOR_REGISTRADO, nombre));
                    socket.close();
                }

                Thread carrera = new Thread(new Carrera(jugadores));
                carrera.start();

            }
        }
    }
}
