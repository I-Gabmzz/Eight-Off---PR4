package Juego;

import Logica.Carta;
import Logica.ListaSimple;

public class ControlDeMovimientos {
    private String tipoMovimiento;
    private int origenIndice;
    private int destinoIndice;
    private int cantidad;

    private ControlDeMovimientos(String tipo, int origen, int destino, int cantidad) {
        this.tipoMovimiento = tipo;
        this.origenIndice = origen;
        this.destinoIndice = destino;
        this.cantidad = cantidad;
    }

    private static final ListaSimple<ControlDeMovimientos> pila = new ListaSimple<>();

    public static ControlDeMovimientos movTableroAReserva(int origen, int destino) {
        return new ControlDeMovimientos("TaR", origen, destino, 1);
    }

    public static ControlDeMovimientos movTableroATablero(int origen, int destino, int cantidad) { // <-- AÑADIDO parámetro cantidad
        return new ControlDeMovimientos("TaT", origen, destino, cantidad); // <-- Guarda la cantidad
    }

    public static ControlDeMovimientos movTableroAFundacion(int origen, int destino) {
        return new ControlDeMovimientos("TaF", origen, destino, 1);
    }

    public static ControlDeMovimientos movReservaATablero(int origen, int destino) {
        return new ControlDeMovimientos("RaT", origen, destino, 1);
    }

    public static ControlDeMovimientos movReservaAFundacion(int origen, int destino) {
        return new ControlDeMovimientos("RaF", origen, destino, 1);
    }

    public static void registrar(ControlDeMovimientos mov) {
        pila.insertaInicio(mov);
    }

    public static boolean puedeDeshacer() {
        return pila.verInicio() != null;
    }

    public static boolean deshacer(Tablero[] pilasTablero, Reserva[] celdasReserva, Foundation[] pilasFundacion) {
        ControlDeMovimientos mov = pila.eliminaInicio();
        if (mov == null) {
            return false;
        }
        Carta carta;

        switch (mov.tipoMovimiento) {
            case "TaR":
                carta = celdasReserva[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    pilasTablero[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "TaT":
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
                carta = pilasFundacion[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    pilasTablero[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "RaT":
                carta = pilasTablero[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    celdasReserva[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "RaF":
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