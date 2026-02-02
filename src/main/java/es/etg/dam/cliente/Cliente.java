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
    private static final String MSG_PUNTOS = "Estado de la carrera = %s";
    ;
    private static final String MSG_FINAL = "%s -> %s";

    public static void main(String[] args) throws IOException {

        try (Socket cliente = new Socket(Servidor.HOST, Servidor.PUERTO)) {

            String nombre = args[PARAM_NOMBRE_JUGADOR];

            Conexion.enviar(nombre, cliente);

            boolean salir = false;

            while (!salir) {
                String mensaje = Conexion.recibir(cliente);
                String msg;

                if (mensaje.equals(MSG_PERDIDO) || mensaje.equals(MSG_GANADO)) {
                    msg = (String.format(MSG_FINAL, nombre, mensaje));
                    salir = true;
                } else {
                    msg = (String.format(MSG_PUNTOS, mensaje));
                }
                System.out.println(msg);
            }
            cliente.close();
        }
    }
}
