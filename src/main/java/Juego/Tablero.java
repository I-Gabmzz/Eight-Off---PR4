package Juego;

import Logica.ListaSimple;
import Logica.Carta;
import Logica.Nodo;

import java.util.ArrayList;

// Clase que representa una de las 8 pilas principales del tablero de juego
public class Tablero {
    private ListaSimple<Carta> cartas; // Lista simple que almacena las cartas apiladas en esta columna

    // Metodo constructor que inicializa la pila del tablero como vacia
    public Tablero() {
        this.cartas = new ListaSimple<>();
    }

    // Funcion que comprueba si una carta puede ser colocada en esta pila
    public boolean sePuedeAgregar(Carta carta) {
        Carta cartaSuperior = this.cartas.verInicio();

        if (cartaSuperior == null) {
            return true;
        }
        boolean mismoPalo = (carta.getPalo() == cartaSuperior.getPalo());
        boolean rangoCorrecto = cartaSuperior.esSiguienteEnRango(carta);
        // Comprueba que sea del mismo palo y el valor sea consecutivo descendente
        return mismoPalo && rangoCorrecto;
    }

    // Se encarga de colocar una carta en la cima de la pila
    public void agregarCarta(Carta carta) {
        this.cartas.insertaInicio(carta);
    }

    // Este metodo saca y devuelve la carta superior de la pila
    public Carta sacarCarta() {
        return this.cartas.eliminaInicio();
    }

    // Devuelve la carta superior de la pila sin eliminarla
    public Carta getCartaSuperior() {
        return this.cartas.verInicio();
    }

    // Metodo que indica si la pila del tablero esta vacia
    public boolean estaVacia() {
        return this.cartas.verInicio() == null;
    }

    // Metodo que devuelve un ArrayList de las cartas en el orden correcto para dibujar
    public ArrayList<Carta> getCartasParaVista() {
        ArrayList<Carta> listaParaVista = new ArrayList<>();
        Nodo<Carta> actual = this.cartas.getInicio();
        while (actual != null) {
            listaParaVista.add(actual.getInfo());
            actual = actual.getSiguiente();
        }
        // Se invierte la lista para que la vista la dibuje desde el fondo a la cima
        java.util.Collections.reverse(listaParaVista);
        return listaParaVista;
    }

    @Override
    public String toString() {
        return this.cartas.mostrarLista();
    }
}