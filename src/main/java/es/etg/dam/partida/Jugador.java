package es.etg.dam.partida;

import java.net.Socket;

import lombok.Getter;

@Getter
public class Jugador {

    private String nombre;
    private int puntos;
    private Socket conexion;

    public Jugador(String nombre, Socket conexion) {
        this.nombre = nombre;
        this.conexion = conexion;
    }

    public void sumar(int puntos) {
        this.puntos += puntos;
    }

}
