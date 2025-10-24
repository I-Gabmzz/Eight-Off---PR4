package Juego;

import Logica.ListaSimple;
import Logica.Carta;

public class Tablero {
    private ListaSimple<Carta> cartas;

    public Tablero() {
        this.cartas = new ListaSimple<>();
    }

    public boolean sePuedeAgregar(Carta carta) {
        Carta cartaSuperior = this.cartas.verInicio();

        if (cartaSuperior == null) {
            return true;
        }
        boolean mismoPalo = (carta.getPalo() == cartaSuperior.getPalo());
        boolean rangoCorrecto = cartaSuperior.esSiguienteEnRango(carta);
        return mismoPalo && rangoCorrecto;
    }

    public void agregarCarta(Carta carta) {
        this.cartas.insertaInicio(carta);
    }

    public Carta sacarCarta() {
        return this.cartas.eliminaInicio();
    }

    public Carta getCartaSuperior() {
        return this.cartas.verInicio();
    }

    public boolean estaVacia() {
        return this.cartas.verInicio() == null;
    }

    @Override
    public String toString() {
        return this.cartas.mostrarLista();
    }
}