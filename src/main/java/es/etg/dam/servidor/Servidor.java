package es.etg.dam.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import es.etg.dam.cliente.Cliente;
import es.etg.dam.common.Conexion;
import es.etg.dam.partida.Carrera;

public class Servidor {

    public static final String HOST = "localhost";
    public static final int PUERTO = 8888;
    public final static int MAX_JUGADORES = 4;

    public static void main(String[] args) throws IOException {

        try (ServerSocket server = new ServerSocket(PUERTO)) {

            while (true) {
                Carrera carrera = new Carrera();
                for (int i = 0; i < MAX_JUGADORES; i++) {
                    Socket socket = server.accept();
                    String nombre = Conexion.recibir(socket);
                    Conexion.enviar(Cliente.OK, socket);
                    carrera.registrarJugadores(nombre, socket);
                }

                Thread hilo = new Thread(carrera);
                hilo.start();
            }
        }
    }
}
