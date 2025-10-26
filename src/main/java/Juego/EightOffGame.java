package Juego;

import Logica.*;
import Logica.ListaSimple;
import java.util.List;

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
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < NUMERO_PILAS_TABLERO; j++) {
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

    public Foundation getPilaFundacion(int indice) { return this.pilasDeFundacion[indice]; }
    public Reserva getCeldaReserva(int indice) { return this.celdasDeReserva[indice]; }
    public Tablero getPilaTablero(int indice) { return this.pilasDeTablero[indice]; }
    public Tablero[] getPilasDeTablero() { return pilasDeTablero; }
    public Reserva[] getCeldasDeReserva() { return celdasDeReserva; }
    public Foundation[] getPilasDeFundacion() { return pilasDeFundacion; }

    private int contarReservasVacias() {
        int contador = 0;
        for (Reserva r : celdasDeReserva) {
            if (r.estaVacia()) {
                contador++;
            }
        }
        return contador;
    }

    private int calcularMaxCartasMovibles(int indiceOrigen, int indiceDestino) {
        int reservasVacias = contarReservasVacias();
        int tablerosVacios = 0;
        boolean destinoEstabaVacio = pilasDeTablero[indiceDestino].estaVacia();

        for (int i = 0; i < pilasDeTablero.length; i++) {
            if (i == indiceOrigen) continue;
            if (destinoEstabaVacio && i == indiceDestino) continue;
            if (pilasDeTablero[i].estaVacia()) {
                tablerosVacios++;
            }
        }
        int maximo = (int) ((reservasVacias + 1) * Math.pow(2, tablerosVacios));
        return maximo;
    }

    private boolean esEscaleraValida(List<Carta> cartas) {
        if (cartas == null || cartas.isEmpty()) return false;
        if (cartas.size() == 1) return true;
        Palo paloBase = cartas.get(0).getPalo();
        for (int i = 1; i < cartas.size(); i++) {
            Carta cartaAbajo = cartas.get(i - 1);
            Carta cartaArriba = cartas.get(i);
            if (cartaArriba.getPalo() != paloBase || cartaArriba.getValor() != cartaAbajo.getValor() - 1) {
                return false;
            }
        }
        return true;
    }

    public boolean moverEscaleraDeTableroATablero(int indiceOrigen, int indiceDestino, int cantidadCartas) {
        if (indiceOrigen == indiceDestino || cantidadCartas <= 0) return false;
        if (indiceOrigen < 0 || indiceOrigen >= pilasDeTablero.length || indiceDestino < 0 || indiceDestino >= pilasDeTablero.length) return false;
        Tablero origen = pilasDeTablero[indiceOrigen];
        Tablero destino = pilasDeTablero[indiceDestino];
        List<Carta> cartasOrigen = origen.getCartasParaVista();
        if (cartasOrigen.size() < cantidadCartas) {
            return false;
        }

        List<Carta> escalera = cartasOrigen.subList(cartasOrigen.size() - cantidadCartas, cartasOrigen.size());
        if (!esEscaleraValida(escalera)) {
            return false;
        }

        Carta cartaInferiorEscalera = escalera.get(0);
        if (!destino.sePuedeAgregar(cartaInferiorEscalera)) {
            return false;
        }

        int maxMovibles = calcularMaxCartasMovibles(indiceOrigen, indiceDestino);
        if (cantidadCartas > maxMovibles) {
            return false;
        }

        ListaSimple<Carta> tempStack = new ListaSimple<>();
        for (int i = 0; i < cantidadCartas; i++) {
            Carta c = origen.sacarCarta();
            if (c != null) tempStack.insertaInicio(c);
            else {
                return false; }
        }
        while (tempStack.verInicio() != null) {
            destino.agregarCarta(tempStack.eliminaInicio());
        }
        ControlDeMovimientos.registrar(
                ControlDeMovimientos.movTableroATablero(indiceOrigen, indiceDestino, cantidadCartas)
        );
        return true;
    }

    public boolean moverDeTableroAReserva(int indiceTablero, int indiceReserva) {
        Tablero origen = this.pilasDeTablero[indiceTablero];
        Reserva destino = this.celdasDeReserva[indiceReserva];
        Carta cartaAMover = origen.getCartaSuperior();
        if (cartaAMover == null || !destino.sePuedeAgregar(cartaAMover)){
            return false;
        }
        origen.sacarCarta();
        destino.agregarCarta(cartaAMover);
        ControlDeMovimientos.registrar(
                ControlDeMovimientos.movTableroAReserva(indiceTablero, indiceReserva)
        );
        return true;
    }

    public boolean moverDeTableroAFundacion(int indiceTablero, int indiceFundacion) {
        Tablero origen = this.pilasDeTablero[indiceTablero];
        Foundation destino = this.pilasDeFundacion[indiceFundacion];
        Carta cartaAMover = origen.getCartaSuperior();
        if (cartaAMover == null || !destino.sePuedeAgregar(cartaAMover)){
            return false;
        }

        origen.sacarCarta();
        destino.agregarCarta(cartaAMover);
        ControlDeMovimientos.registrar(
                ControlDeMovimientos.movTableroAFundacion(indiceTablero, indiceFundacion)
        );
        return true;
    }

    public boolean moverDeReservaATablero(int indiceReserva, int indiceTablero) {
        Reserva origen = this.celdasDeReserva[indiceReserva];
        Tablero destino = this.pilasDeTablero[indiceTablero];
        Carta cartaAMover = origen.getCarta();
        if (cartaAMover == null || !destino.sePuedeAgregar(cartaAMover)){
            return false;
        }
        origen.sacarCarta();
        destino.agregarCarta(cartaAMover);
        ControlDeMovimientos.registrar(
                ControlDeMovimientos.movReservaATablero(indiceReserva, indiceTablero)
        );
        return true;
    }

    public boolean moverDeReservaAFundacion(int indiceReserva, int indiceFundacion) {
        Reserva origen = this.celdasDeReserva[indiceReserva];
        Foundation destino = this.pilasDeFundacion[indiceFundacion];
        Carta cartaAMover = origen.getCarta();
        if (cartaAMover == null || !destino.sePuedeAgregar(cartaAMover)){
            return false;
        }

        origen.sacarCarta();
        destino.agregarCarta(cartaAMover);
        ControlDeMovimientos.registrar(
                ControlDeMovimientos.movReservaAFundacion(indiceReserva, indiceFundacion)
        );
        return true;
    }

    public MovimientoPosible buscarPista() {
        for (int r = 0; r < celdasDeReserva.length; r++) {
            Reserva origenR = celdasDeReserva[r];
            if (!origenR.estaVacia()) {
                Carta cartaR = origenR.getCarta();
                for (int f = 0; f < pilasDeFundacion.length; f++) {
                    if (pilasDeFundacion[f].sePuedeAgregar(cartaR)) {
                        return new MovimientoPosible(MovimientoPosible.TIPO_ORIGEN_RESERVA, r, MovimientoPosible.TIPO_DESTINO_FUNDACION, f);
                    }
                }
            }
        }
        for (int t = 0; t < pilasDeTablero.length; t++) {
            Tablero origenT = pilasDeTablero[t];
            Carta cartaT = origenT.getCartaSuperior();
            if (cartaT != null) {
                for (int f = 0; f < pilasDeFundacion.length; f++) {
                    if (pilasDeFundacion[f].sePuedeAgregar(cartaT)) {
                        return new MovimientoPosible(MovimientoPosible.TIPO_ORIGEN_TABLERO, t, MovimientoPosible.TIPO_DESTINO_FUNDACION, f);
                    }
                }
            }
        }

        for (int r = 0; r < celdasDeReserva.length; r++) {
            Reserva origenR = celdasDeReserva[r];
            if (!origenR.estaVacia()) {
                Carta cartaR = origenR.getCarta();
                for (int tDest = 0; tDest < pilasDeTablero.length; tDest++) {
                    if (pilasDeTablero[tDest].sePuedeAgregar(cartaR)) {
                        return new MovimientoPosible(MovimientoPosible.TIPO_ORIGEN_RESERVA, r, MovimientoPosible.TIPO_DESTINO_TABLERO, tDest);
                    }
                }
            }
        }

        for (int tOrigen = 0; tOrigen < pilasDeTablero.length; tOrigen++) {
            Tablero origenT = pilasDeTablero[tOrigen];
            List<Carta> cartasOrigen = origenT.getCartasParaVista();
            if (cartasOrigen.isEmpty()) continue;

            for (int k = cartasOrigen.size() - 1; k >= 0; k--) {
                int cantidadEscalera = cartasOrigen.size() - k;
                List<Carta> escalera = cartasOrigen.subList(k, cartasOrigen.size());

                if (esEscaleraValida(escalera)) {
                    Carta cartaInferiorEscalera = escalera.get(0);

                    for (int tDest = 0; tDest < pilasDeTablero.length; tDest++) {
                        if (tOrigen == tDest) continue;
                        Tablero destinoT = pilasDeTablero[tDest];

                        if (destinoT.sePuedeAgregar(cartaInferiorEscalera)) {
                            int maxMovibles = calcularMaxCartasMovibles(tOrigen, tDest);

                            if (cantidadEscalera <= maxMovibles) {
                                return new MovimientoPosible(MovimientoPosible.TIPO_ORIGEN_TABLERO, tOrigen, MovimientoPosible.TIPO_DESTINO_TABLERO, tDest, cantidadEscalera);
                            }
                        }
                    }
                } else {

                    break;
                }
            }
        }

        int primeraReservaVacia = -1;
        for(int r=0; r < celdasDeReserva.length; r++){
            if(celdasDeReserva[r].estaVacia()){ primeraReservaVacia = r; break; }
        }
        if(primeraReservaVacia != -1){
            for (int t = 0; t < pilasDeTablero.length; t++) {
                Carta cartaT = pilasDeTablero[t].getCartaSuperior();
                if (cartaT != null) {
                    boolean puedeIrAFundacion = false;
                    for (int f = 0; f < pilasDeFundacion.length; f++) { if (pilasDeFundacion[f].sePuedeAgregar(cartaT)) { puedeIrAFundacion = true; break; } }
                    boolean puedeIrATablero = false;
                    for (int tDest = 0; tDest < pilasDeTablero.length; tDest++) { if (t != tDest && pilasDeTablero[tDest].sePuedeAgregar(cartaT)) { puedeIrATablero = true; break; } }

                    if (!puedeIrAFundacion && !puedeIrATablero) {
                        return new MovimientoPosible(MovimientoPosible.TIPO_ORIGEN_TABLERO, t, MovimientoPosible.TIPO_DESTINO_RESERVA, primeraReservaVacia);
                    }
                }
            }
        }
        return null;
    }

}