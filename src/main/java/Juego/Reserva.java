package Juego;

import Logica.Carta;
import Logica.ListaSimple;

// Clase que representa una celda de reserva del juego
public class Reserva {
    private ListaSimple<Carta> celda; // Lista simple que almacena la unica carta en la celda

    // Metodo constructor que inicializa la celda de reserva como vacia
    public Reserva() {
        this.celda = new ListaSimple<>();
    }

    // Metodo que indica si la celda de reserva esta vacia
    public boolean estaVacia() {
        return this.celda.verInicio() == null;
    }

    // Funcion que comprueba si se puede agregar una carta
    public boolean sePuedeAgregar(Carta carta) {
        return estaVacia();
    }

    // Se encarga de colocar una carta en la celda si esta se encuentra vacia
    public void agregarCarta(Carta carta) {
        if (estaVacia()) {
            this.celda.insertaInicio(carta); // push
        }
    }

    // Este metodo saca y devuelve la carta de la celda (pop)
    public Carta sacarCarta() {
        return this.celda.eliminaInicio(); // pop
    }

    // Devuelve la carta de la celda sin eliminarla
    public Carta getCarta() {
        return this.celda.verInicio(); // peek
    }
}