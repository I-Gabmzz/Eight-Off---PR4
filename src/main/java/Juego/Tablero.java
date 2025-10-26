package Juego;

import Logica.ListaSimple;
import Logica.Carta;
import Logica.Nodo;

import java.util.ArrayList;

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

    public ArrayList<Carta> getCartasParaVista() {
        ArrayList<Carta> listaParaVista = new ArrayList<>();
        Nodo<Carta> actual = this.cartas.getInicio();
        while (actual != null) {
            listaParaVista.add(actual.getInfo());
            actual = actual.getSiguiente();
        }
        java.util.Collections.reverse(listaParaVista);
        return listaParaVista;
    }

    @Override
    public String toString() {
        return this.cartas.mostrarLista();
    }
}