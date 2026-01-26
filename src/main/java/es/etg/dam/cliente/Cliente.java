package es.etg.dam.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import es.etg.dam.conexion.Conexion;

public class Cliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 8888;
    private static final String NOMBRE = "Introduce el nombre del caballo: ";
    public static final String OK = "OK";
    private static final String MSG_ERROR_REGISTRO = "No se pudo registrar";
    private static final String MSG_REGISTRO = "Registrado correctamente como: ";
    public static final String MSG_GANADO = "ENHORABUENA";
    public static final String MSG_PERDIDO = "GAME OVER";
    private static final String MSG_PUNTOS = "Estado de la carrera = ";

    public static void main(String[] args) throws IOException {

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        System.out.print(NOMBRE);
        String nombre = teclado.readLine();

        Socket cliente = new Socket(HOST, PUERTO);

        // Enviar nombre al servidor
        Conexion.enviar(nombre, cliente);

        // Esperar OK
        String respuesta = Conexion.recibir(cliente);
        if (!respuesta.equals(OK)) {
            System.out.println(MSG_ERROR_REGISTRO);
            cliente.close();
            return;
        }

        System.out.println(MSG_REGISTRO + nombre);

        // Recibir mensajes de la carrera
        while (true) {
            String mensaje = Conexion.recibir(cliente);

            if (mensaje.equals(MSG_PERDIDO) || (mensaje.equals(MSG_GANADO))) {
                System.out.println(mensaje);
                break;
            }

            // Mensaje de puntos
            System.out.println(MSG_PUNTOS + mensaje);
        }

        cliente.close();

    }
}
