package Juego;

import Logica.Carta;
import Logica.ListaSimple;

// Clase que gestiona el historial de movimientos para la funcion de deshacer
public class ControlDeMovimientos {
    private String tipoMovimiento; // Almacena el tipo de movimiento
    private int origenIndice; // Indice de la pila o celda de origen
    private int destinoIndice; // Indice de la pila o celda de destino
    private int cantidad; // Cantidad de cartas movidas

    // Constructor privado que inicializa un nuevo objeto de movimiento
    private ControlDeMovimientos(String tipo, int origen, int destino, int cantidad) {
        this.tipoMovimiento = tipo;
        this.origenIndice = origen;
        this.destinoIndice = destino;
        this.cantidad = cantidad;
    }

    // Pila basada en ListaSimple que almacena el historial de movimientos
    private static final ListaSimple<ControlDeMovimientos> pila = new ListaSimple<>();

    // Metodo para crear un movimiento de Tablero a Reserva
    public static ControlDeMovimientos movTableroAReserva(int origen, int destino) {
        return new ControlDeMovimientos("TaR", origen, destino, 1);
    }

    // Metodo para crear un movimiento de Tablero a Tablero (escalera)
    public static ControlDeMovimientos movTableroATablero(int origen, int destino, int cantidad) { // <-- AÑADIDO parametro cantidad
        return new ControlDeMovimientos("TaT", origen, destino, cantidad); // <-- Guarda la cantidad
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

    // Metodo que añade un movimiento a la pila del historial
    public static void registrar(ControlDeMovimientos mov) {
        pila.insertaInicio(mov);
    }

    // Funcion que comprueba si la pila de historial tiene movimientos para deshacer
    public static boolean puedeDeshacer() {
        return pila.verInicio() != null;
    }

    // Metodo principal que revierte el ultimo movimiento realizado en el juego
    public static boolean deshacer(Tablero[] pilasTablero, Reserva[] celdasReserva, Foundation[] pilasFundacion) {
        ControlDeMovimientos mov = pila.eliminaInicio();
        if (mov == null) {
            return false;
        }
        Carta carta;

        switch (mov.tipoMovimiento) {
            case "TaR":
                // Revertir Tablero a Reserva: Mueve la carta de la reserva de vuelta al tablero
                carta = celdasReserva[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    pilasTablero[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "TaT":
                // Revertir Tablero a Tablero: Mueve la escalera de vuelta a su columna original
                Tablero origenT = pilasTablero[mov.origenIndice];
                Tablero destinoT = pilasTablero[mov.destinoIndice];
                ListaSimple<Carta> almacen = new ListaSimple<>();
                for (int i = 0; i < mov.cantidad; i++) {
                    Carta c = destinoT.sacarCarta();
                    if (c != null) {
                        almacen.insertaInicio(c);
                    } else {
                        while(almacen.verInicio() != null) {
                            destinoT.agregarCarta(almacen.eliminaInicio());
                        }
                        return false;
                    }
                }
                while (almacen.verInicio() != null) {
                    origenT.agregarCarta(almacen.eliminaInicio());
                }
                break;

            case "TaF":
                // Revertir Tablero a Fundacion: Mueve la carta de la foundation de vuelta al tablero
                carta = pilasFundacion[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    pilasTablero[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "RaT":
                // Revertir Reserva a Tablero: Mueve la carta del tablero de vuelta a la reserva
                carta = pilasTablero[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    celdasReserva[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "RaF":
                // Revertir Reserva a Fundacion: Mueve la carta del foundation de vuelta a la reserva
                carta = pilasFundacion[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    celdasReserva[mov.origenIndice].agregarCarta(carta);
                }
                break;

            default:
                return false;
        }
        return true;
    }

}