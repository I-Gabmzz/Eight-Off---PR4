package Juego;

import Logica.Carta;
import Logica.ListaSimple;

public class Reserva {
    private ListaSimple<Carta> celda;

    public Reserva() {
        this.celda = new ListaSimple<>();
    }

    public boolean estaVacia() {
        return this.celda.verInicio() == null;
    }

    public boolean sePuedeAgregar(Carta carta) {
        return estaVacia();
    }

    public void agregarCarta(Carta carta) {
        if (estaVacia()) {
            this.celda.insertaInicio(carta); // "push"
        }
    }

    public Carta sacarCarta() {
        return this.celda.eliminaInicio(); // "pop"
    }

    public Carta getCarta() {
        return this.celda.verInicio(); // "peek"
    }
}