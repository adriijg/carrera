package es.etg.dam.partida;

import java.net.Socket;

public class Jugador {

    private String nombre;
    private int puntos;
    private Socket conexion;

    public Jugador(String nombre, Socket conexion) {
        nombre = this.nombre;
        conexion = this.conexion;
    }

    public void sumar(int puntos) {
        puntos += this.puntos;
    }

}
