package Logica;

import java.util.ArrayList;
import java.util.Collections;

public class Mazo {
    private ListaDobleCircular<Carta> cartas;

    public Mazo() {
        this.cartas = new ListaDobleCircular<>();
        this.inicializarMazoCompleto();
    }

    private void inicializarMazoCompleto() {
        ArrayList<Carta> mazoTemporal = new ArrayList<>();
        for (Palo palo : Palo.values()) {
            for (int valor = 2; valor <= 14; valor++) {
                Carta nuevaCarta = new Carta(valor, palo);
                nuevaCarta.ponerBocaArriba();
                mazoTemporal.add(nuevaCarta);
            }
        }
        Collections.shuffle(mazoTemporal);
        for (Carta carta : mazoTemporal) {
            this.cartas.insertaFin(carta);
        }
    }

    public Carta repartirCarta() {
        return this.cartas.eliminaInicio();
    }


    @Override
    public String toString() {
        return this.cartas.mostrarLista();
    }
}