package Juego;

import Logica.Carta;
import Logica.ListaDoble;
import Logica.ListaSimple;
import Logica.NodoDoble;
import java.util.ArrayList;

// Clase que gestiona el historial de movimientos para Undo y Redo
public class ControlDeMovimientos {
    private String tipoMovimiento; // Almacena el tipo de movimiento
    private int origenIndice; // Indice de la pila o celda de origen
    private int destinoIndice; // Indice de la pila o celda de destino
    private int cantidad; // Cantidad de cartas movidas

    // Reemplaza la ListaSimple por la ListaDoble para el historial
    private static ListaDoble<ControlDeMovimientos> historial = new ListaDoble<>();

    // Puntero que indica donde estamos en el historial
    private static NodoDoble<ControlDeMovimientos> posicionActual = null;

    // Puntero para saber donde estaba el juego antes de abrir el historial
    private static NodoDoble<ControlDeMovimientos> posicionGuardada = null;

    // Constructor privado que inicializa un nuevo objeto de movimiento
    private ControlDeMovimientos(String tipo, int origen, int destino, int cantidad) {
        this.tipoMovimiento = tipo;
        this.origenIndice = origen;
        this.destinoIndice = destino;
        this.cantidad = cantidad;
    }

    // Metodo para crear un movimiento de Tablero a Reserva
    public static ControlDeMovimientos movTableroAReserva(int origen, int destino) {
        return new ControlDeMovimientos("TaR", origen, destino, 1);
    }

    // Metodo para crear un movimiento de Tablero a Tablero
    public static ControlDeMovimientos movTableroATablero(int origen, int destino, int cantidad) {
        return new ControlDeMovimientos("TaT", origen, destino, cantidad);
    }

    // Funcion que crea un movimiento de Tablero a Fundacion
    public static ControlDeMovimientos movTableroAFundacion(int origen, int destino) {
        return new ControlDeMovimientos("TaF", origen, destino, 1);
    }

    // Se encarga de crear un movimiento de Reserva a Tablero
    public static ControlDeMovimientos movReservaATablero(int origen, int destino) {
        return new ControlDeMovimientos("RaT", origen, destino, 1);
    }

    // Este metodo crea un movimiento de Reserva a Fundacion
    public static ControlDeMovimientos movReservaAFundacion(int origen, int destino) {
        return new ControlDeMovimientos("RaF", origen, destino, 1);
    }


    // Metodo que añade un movimiento nuevo al historial
    public static void registrar(ControlDeMovimientos mov) {
        if (posicionActual != null && posicionActual.getSiguiente() != null) {
            historial.truncarDesde(posicionActual);
        }

        historial.insertarFin(mov);
        posicionActual = historial.getFin();
    }

    // Funcion que comprueba si podemos deshacer
    public static boolean puedeDeshacer() {
        return posicionActual != null;
    }

    // Funcion que comprueba si podemos rehacer
    public static boolean puedeRehacer() {
        if (historial.estaVacia()) return false;
        if (posicionActual == null) {
            return historial.getInicio() != null;
        }
        return posicionActual.getSiguiente() != null;
    }

    // Metodo que revierte el ultimo movimiento
    public static boolean deshacer(Tablero[] pilasTablero, Reserva[] celdasReserva, Foundation[] pilasFundacion) {
        if (!puedeDeshacer()) {
            return false;
        }
        ControlDeMovimientos mov = posicionActual.getInfo();
        boolean exito = ejecutarDeshacer(mov, pilasTablero, celdasReserva, pilasFundacion);

        if (exito) {
            posicionActual = posicionActual.getAnterior();
        }
        return exito;
    }

    // Metodo que aplica un movimiento deshecho
    public static boolean rehacer(Tablero[] pilasTablero, Reserva[] celdasReserva, Foundation[] pilasFundacion) {
        if (!puedeRehacer()) {
            return false;
        }

        if (posicionActual == null) {
            posicionActual = historial.getInicio();
        } else {
            posicionActual = posicionActual.getSiguiente();
        }

        ControlDeMovimientos mov = posicionActual.getInfo();
        return ejecutarRehacer(mov, pilasTablero, celdasReserva, pilasFundacion);
    }


    // Logica para revertir un movimiento
    private static boolean ejecutarDeshacer(ControlDeMovimientos mov, Tablero[] pilasTablero, Reserva[] celdasReserva, Foundation[] pilasFundacion) {
        Carta carta;
        switch (mov.tipoMovimiento) {
            case "TaR":
                carta = celdasReserva[mov.destinoIndice].sacarCarta();
                if (carta != null) pilasTablero[mov.origenIndice].agregarCarta(carta);
                break;
            case "TaT":
                Tablero origenT = pilasTablero[mov.origenIndice];
                Tablero destinoT = pilasTablero[mov.destinoIndice];
                ListaSimple<Carta> almacen = new ListaSimple<>();
                for (int i = 0; i < mov.cantidad; i++) {
                    Carta c = destinoT.sacarCarta();
                    if (c != null) almacen.insertaInicio(c);
                }
                while (almacen.verInicio() != null) {
                    origenT.agregarCarta(almacen.eliminaInicio());
                }
                break;
            case "TaF":
                carta = pilasFundacion[mov.destinoIndice].sacarCarta();
                if (carta != null) pilasTablero[mov.origenIndice].agregarCarta(carta);
                break;
            case "RaT":
                carta = pilasTablero[mov.destinoIndice].sacarCarta();
                if (carta != null) celdasReserva[mov.origenIndice].agregarCarta(carta);
                break;
            case "RaF":
                carta = pilasFundacion[mov.destinoIndice].sacarCarta();
                if (carta != null) celdasReserva[mov.origenIndice].agregarCarta(carta);
                break;
            default:
                return false;
        }
        return true;
    }

    // Logica para ejecutar un movimiento
    private static boolean ejecutarRehacer(ControlDeMovimientos mov, Tablero[] pilasTablero, Reserva[] celdasReserva, Foundation[] pilasFundacion) {
        Carta carta;
        switch (mov.tipoMovimiento) {
            case "TaR": // Tablero a Reserva
                carta = pilasTablero[mov.origenIndice].sacarCarta();
                if (carta != null) celdasReserva[mov.destinoIndice].agregarCarta(carta);
                break;
            case "TaT": // Tablero a Tablero
                ListaSimple<Carta> almacen = new ListaSimple<>();
                for (int i = 0; i < mov.cantidad; i++) {
                    Carta c = pilasTablero[mov.origenIndice].sacarCarta();
                    if (c != null) almacen.insertaInicio(c);
                }
                while (almacen.verInicio() != null) {
                    pilasTablero[mov.destinoIndice].agregarCarta(almacen.eliminaInicio());
                }
                break;
            case "TaF": // Tablero a Fundacion
                carta = pilasTablero[mov.origenIndice].sacarCarta();
                if (carta != null) pilasFundacion[mov.destinoIndice].agregarCarta(carta);
                break;
            case "RaT": // Reserva a Tablero
                carta = celdasReserva[mov.origenIndice].sacarCarta();
                if (carta != null) pilasTablero[mov.destinoIndice].agregarCarta(carta);
                break;
            case "RaF": // Reserva a Fundacion
                carta = celdasReserva[mov.origenIndice].sacarCarta();
                if (carta != null) pilasFundacion[mov.destinoIndice].agregarCarta(carta);
                break;
            default:
                return false;
        }
        return true;
    }

    // Devuelve todos los nodos para el historial
    public static ArrayList<NodoDoble<ControlDeMovimientos>> getHistorialNodos() {
        return historial.getNodos();
    }

    // Devuelve el puntero actual
    public static NodoDoble<ControlDeMovimientos> getPosicionActual() {
        return posicionActual;
    }

    // Guarda el estado del juego antes de abrir el historial
    public static void guardarPosicion() {
        posicionGuardada = posicionActual;
    }

    // Restaura el estado del juego a como estaba antes de abrir el historial
    public static void restaurarPosicion(Tablero[] pilasTablero, Reserva[] celdasReserva, Foundation[] pilasFundacion) {
        while (posicionActual != posicionGuardada) {
            if (historial.estaVacia() || posicionGuardada == null) break;

            while (posicionActual != posicionGuardada && puedeRehacer()) {
                rehacer(pilasTablero, celdasReserva, pilasFundacion);
            }

            while (posicionActual != posicionGuardada && puedeDeshacer()) {
                deshacer(pilasTablero, celdasReserva, pilasFundacion);
            }
        }
    }

    // Aplica el estado del historial
    public static void aplicarPosicion() {
        posicionGuardada = posicionActual;
    }

    // Metodo para formatear el movimiento para el historial
    @Override
    public String toString() {
        String origen = (tipoMovimiento.startsWith("T")) ? "Tablero " + (origenIndice + 1) : "Reserva " + (origenIndice + 1);
        String destino = "";
        switch (tipoMovimiento.substring(tipoMovimiento.indexOf('a') + 1)) {
            case "R": destino = "Reserva " + (destinoIndice + 1); break;
            case "T": destino = "Tablero " + (destinoIndice + 1); break;
            case "F": destino = "Fundacion " + (destinoIndice + 1); break;
        }
        return String.format("%s -> %s (%d carta%s)", origen, destino, cantidad, (cantidad > 1 ? "s" : ""));
    }

    public static void resetHistorial() {
        historial = new ListaDoble<>();
        posicionActual = null;
        posicionGuardada = null;
    }

}