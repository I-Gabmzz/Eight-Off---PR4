package Juego;

import Logica.Carta;
import Logica.Mazo;

public class EightOffGame {
    private static final int NUMERO_PILAS_FUNDACION = 4;
    private static final int NUMERO_CELDAS_RESERVA = 8;
    private static final int NUMERO_PILAS_TABLERO = 8;

    private Mazo mazo;
    private Foundation[] pilasDeFundacion;
    private Reserva[] celdasDeReserva;
    private Tablero[] pilasDeTablero;


    public EightOffGame() {
        this.mazo = new Mazo();
        this.pilasDeFundacion = new Foundation[NUMERO_PILAS_FUNDACION];
        this.celdasDeReserva = new Reserva[NUMERO_CELDAS_RESERVA];
        this.pilasDeTablero = new Tablero[NUMERO_PILAS_TABLERO];

        for (int i = 0; i < NUMERO_PILAS_FUNDACION; i++) {
            this.pilasDeFundacion[i] = new Foundation();
        }
        for (int i = 0; i < NUMERO_CELDAS_RESERVA; i++) {
            this.celdasDeReserva[i] = new Reserva();
        }
        for (int i = 0; i < NUMERO_PILAS_TABLERO; i++) {
            this.pilasDeTablero[i] = new Tablero();
        }
        this.repartirCartasIniciales();
    }

    private void repartirCartasIniciales() {
        for (int i = 0; i < 6; i++) { // 6 "filas"
            for (int j = 0; j < NUMERO_PILAS_TABLERO; j++) { // 8 columnas
                Carta carta = this.mazo.repartirCarta();
                if (carta != null) {
                    this.pilasDeTablero[j].agregarCarta(carta);
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            Carta carta = this.mazo.repartirCarta();
            if (carta != null) {
                this.celdasDeReserva[i].agregarCarta(carta);
            }
        }
    }

    public Foundation getPilaFundacion(int indice) {
        return this.pilasDeFundacion[indice];
    }

    public Reserva getCeldaReserva(int indice) {
        return this.celdasDeReserva[indice];
    }

    public Tablero getPilaTablero(int indice) {
        return this.pilasDeTablero[indice];
    }

    public boolean moverDeTableroATablero(int indiceOrigen, int indiceDestino) {
        if (indiceOrigen == indiceDestino) {
            return false;
        }
        Tablero origen = this.pilasDeTablero[indiceOrigen];
        Tablero destino = this.pilasDeTablero[indiceDestino];
        Carta cartaAMover = origen.getCartaSuperior(); // peek
        if (cartaAMover == null) {
            return false;
        }
        if (destino.sePuedeAgregar(cartaAMover)) {
            origen.sacarCarta();
            destino.agregarCarta(cartaAMover);
            ControlDeMovimientos.registrar(ControlDeMovimientos.movTableroATablero(indiceOrigen, indiceDestino));
            return true;
        }
        return false;
    }

    public boolean moverDeTableroAReserva(int indiceTablero, int indiceReserva) {
        Tablero origen = this.pilasDeTablero[indiceTablero];
        Reserva destino = this.celdasDeReserva[indiceReserva];
        Carta cartaAMover = origen.getCartaSuperior();
        if (cartaAMover == null) {
            return false;
        }
        if (destino.sePuedeAgregar(cartaAMover)) {
            origen.sacarCarta();
            destino.agregarCarta(cartaAMover);
            ControlDeMovimientos.registrar(ControlDeMovimientos.movTableroAReserva(indiceTablero, indiceReserva));
            return true;
        }

        return false;
    }

    public boolean moverDeTableroAFundacion(int indiceTablero, int indiceFundacion) {
        Tablero origen = this.pilasDeTablero[indiceTablero];
        Foundation destino = this.pilasDeFundacion[indiceFundacion];
        Carta cartaAMover = origen.getCartaSuperior();
        if (cartaAMover == null) {
            return false;
        }
        if (destino.sePuedeAgregar(cartaAMover)) {
            origen.sacarCarta();
            destino.agregarCarta(cartaAMover);
            ControlDeMovimientos.registrar(ControlDeMovimientos.movTableroAFundacion(indiceTablero, indiceFundacion));
            return true;
        }
        return false;
    }

    public boolean moverDeReservaATablero(int indiceReserva, int indiceTablero) {
        Reserva origen = this.celdasDeReserva[indiceReserva];
        Tablero destino = this.pilasDeTablero[indiceTablero];
        Carta cartaAMover = origen.getCarta(); // peek
        if (cartaAMover == null) {
            return false;
        }
        if (destino.sePuedeAgregar(cartaAMover)) {
            origen.sacarCarta();
            destino.agregarCarta(cartaAMover);
            ControlDeMovimientos.registrar(ControlDeMovimientos.movReservaATablero(indiceReserva, indiceTablero));
            return true;
        }
        return false;
    }

    public boolean moverDeReservaAFundacion(int indiceReserva, int indiceFundacion) {
        Reserva origen = this.celdasDeReserva[indiceReserva];
        Foundation destino = this.pilasDeFundacion[indiceFundacion];
        Carta cartaAMover = origen.getCarta();
        if (cartaAMover == null) {
            return false;
        }
        if (destino.sePuedeAgregar(cartaAMover)) {
            origen.sacarCarta();
            destino.agregarCarta(cartaAMover);
            ControlDeMovimientos.registrar(ControlDeMovimientos.movReservaAFundacion(indiceReserva, indiceFundacion));
            return true;
        }
        return false;
    }

    public boolean deshacerMovimiento() {
        return ControlDeMovimientos.deshacer(this.pilasDeTablero, this.celdasDeReserva, this.pilasDeFundacion);
    }

}