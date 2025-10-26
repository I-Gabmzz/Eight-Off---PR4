package Juego;

import Logica.Carta;
import Logica.ListaSimple;
import Logica.Palo;

// Clase que representa una de las 4 pilas de fundacion (donde se apilan los palos del As al Rey)
public class Foundation {
    private ListaSimple<Carta> cartas; // Lista que almacena las cartas apiladas en esta fundacion

    // Metodo constructor que inicializa la pila de cartas vacia
    public Foundation() {
        this.cartas = new ListaSimple<>();
    }

    // Funcion que comprueba si una carta puede ser legalmente colocada en esta fundacion
    public boolean sePuedeAgregar(Carta carta) {
        Carta cartaSuperior = this.cartas.verInicio(); // Usando el "peek"
        int valorNuevo = carta.getValorBajo();
        if (cartaSuperior == null) {
            // Si esta vacia, solo acepta un As
            return valorNuevo == 1;
        }
        int valorSuperior = cartaSuperior.getValorBajo();
        boolean mismoPalo = (carta.getPalo() == cartaSuperior.getPalo());
        boolean rangoCorrecto = (valorSuperior + 1 == valorNuevo);
        // Comprueba que sea del mismo palo y el valor sea consecutivo
        return mismoPalo && rangoCorrecto;
    }

    // Se encarga de colocar una carta en la cima de la pila del foundation
    public void agregarCarta(Carta carta) {
        this.cartas.insertaInicio(carta);
    }

    // Metodo que saca y devuelve la carta superior
    public Carta sacarCarta() {
        return this.cartas.eliminaInicio();
    }

    // Este metodo devuelve la carta superior de la pila sin eliminarla, es decir, un peek
    public Carta getCartaSuperior() {
        return this.cartas.verInicio();
    }

    // Metodo que indica si la pila de fundacion esta vacia
    public boolean estaVacia() {
        return this.cartas.verInicio() == null;
    }

    // Devuelve el palo de las cartas que se estan apilando en esta fundacion
    public Palo getPalo() {
        if (estaVacia()) {
            return null;
        }
        return getCartaSuperior().getPalo();
    }
}