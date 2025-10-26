package Juego;

import Logica.Carta;
import Logica.ListaSimple;
import Logica.Palo;

public class Foundation {
    private ListaSimple<Carta> cartas;

    public Foundation() {
        this.cartas = new ListaSimple<>();
    }

    public boolean sePuedeAgregar(Carta carta) {
        Carta cartaSuperior = this.cartas.verInicio(); // Usando el "peek"
        int valorNuevo = carta.getValorBajo();
        if (cartaSuperior == null) {
            return valorNuevo == 1;
        }
        int valorSuperior = cartaSuperior.getValorBajo();
        boolean mismoPalo = (carta.getPalo() == cartaSuperior.getPalo());
        boolean rangoCorrecto = (valorSuperior + 1 == valorNuevo);
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

    public Palo getPalo() {
        if (estaVacia()) {
            return null;
        }
        return getCartaSuperior().getPalo();
    }
}