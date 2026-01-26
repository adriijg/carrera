package es.etg.dam.partida;

import java.net.Socket;

import lombok.Getter;

@Getter

public class Jugador {

    private static final int CIEN = 100;
    private String nombre;
    private int puntos;
    private Socket conexion;

    public Jugador(String nombre, Socket conexion) {
        this.nombre = nombre;
        this.conexion = conexion;
    }

    public void sumar(int puntos) {
        this.puntos += puntos;
        if (this.puntos > CIEN) {
            this.puntos = CIEN;
        }
    }
}
