package es.etg.dam.cliente;

import java.io.IOException;
import java.net.Socket;

import es.etg.dam.common.Conexion;
import es.etg.dam.servidor.Servidor;

public class Cliente {

    public static final String OK = "OK";
    public static final int PARAM_NOMBRE_JUGADOR = 0;
    private static final String MSG_ERROR_REGISTRO = "No se pudo registrar ";
    public static final String MSG_GANADO = "ENHORABUENA";
    public static final String MSG_PERDIDO = "GAME OVER";
    private static final String MSG_PUNTOS = "Estado de la carrera = %s";;

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            throw new IllegalArgumentException();
        }

        String nombre = args[PARAM_NOMBRE_JUGADOR];

        try (Socket cliente = new Socket(Servidor.HOST, Servidor.PUERTO)) {

            boolean registrado = false;

            String respuesta = Conexion.recibir(cliente);
            if (OK.equals(respuesta)) {
                registrado = true;
            }

            if (!registrado) {
                System.err.println(MSG_ERROR_REGISTRO);

            }

            boolean salir = false;

            while (!salir) {
                String mensaje = Conexion.recibir(cliente);

                if (mensaje.equals(MSG_PERDIDO) || (mensaje.equals(MSG_GANADO))) {
                    System.out.println(mensaje);
                    salir = true;
                } else {
                    System.out.println(String.format(MSG_PUNTOS, mensaje));
                }
            }
            cliente.close();
        }
    }
}
