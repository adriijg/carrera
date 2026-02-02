package es.etg.dam.cliente;

import java.io.IOException;
import java.net.Socket;

import es.etg.dam.common.Conexion;
import es.etg.dam.servidor.Servidor;

public class Cliente {

    public static final String OK = "OK";
    public static final int PARAM_NOMBRE_JUGADOR = 0;
    public static final String MSG_GANADO = "ENHORABUENA";
    public static final String MSG_PERDIDO = "GAME OVER";
    private static final String MSG_PUNTOS = "Estado de la carrera = %s";;
    private static final String FLECHA = "%s -> %s";

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new IllegalArgumentException();
        }

        String nombre = args[PARAM_NOMBRE_JUGADOR];

        try (Socket cliente = new Socket(Servidor.HOST, Servidor.PUERTO)) {

            Conexion.enviar(nombre, cliente);

            boolean salir = false;

            while (!salir) {
                String mensaje = Conexion.recibir(cliente);

                if (mensaje.equals(MSG_PERDIDO) || mensaje.equals(MSG_GANADO)) {
                    System.out.println(String.format(nombre + " -> " + mensaje));
                    salir = true;
                } else {
                    System.out.println(String.format(MSG_PUNTOS, mensaje));
                }
            }
            cliente.close();
        }
    }
}
