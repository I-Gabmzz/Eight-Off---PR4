package Logica;

import java.util.ArrayList;
import java.util.Collections;

// Clase que representa el mazo o baraja de 52 cartas
public class Mazo {
    private ListaDobleCircular<Carta> cartas; // Lista doble circular que almacena las cartas del mazo

    // Metodo constructor que inicializa el mazo y lo baraja
    public Mazo() {
        this.cartas = new ListaDobleCircular<>();
        this.inicializarMazoCompleto();
    }

    // Se encarga de crear las 52 cartas, barajarlas y meterlas a la lista
    private void inicializarMazoCompleto() {
        ArrayList<Carta> mazoTemporal = new ArrayList<>();
        for (Palo palo : Palo.values()) {
            for (int valor = 2; valor <= 14; valor++) {
                Carta nuevaCarta = new Carta(valor, palo);
                nuevaCarta.ponerBocaArriba();
                mazoTemporal.add(nuevaCarta);
            }
        }
        // Baraja las cartas usando el metodo shuffle de Collections
        Collections.shuffle(mazoTemporal);
        // Inserta las cartas barajadas en la lista doble circular
        for (Carta carta : mazoTemporal) {
            this.cartas.insertaFin(carta);
        }
    }

    // Metodo que saca y devuelve una carta del inicio del mazo
    public Carta repartirCarta() {
        return this.cartas.eliminaInicio();
    }

    // Devuelve una representacion en String de las cartas restantes en el mazo
    @Override
    public String toString() {
        return this.cartas.mostrarLista();
    }
}