package Juego;

import Logica.Carta;
import Logica.ListaSimple;

public class ControlDeMovimientos {
    private String tipoMovimiento;
    private int origenIndice;
    private int destinoIndice;
    private static final ListaSimple<ControlDeMovimientos> pila = new ListaSimple<>();

    private ControlDeMovimientos(String tipo, int origen, int destino) {
        this.tipoMovimiento = tipo;
        this.origenIndice = origen;
        this.destinoIndice = destino;
    }

    public static ControlDeMovimientos movTableroAReserva(int origen, int destino) {
        return new ControlDeMovimientos("TR", origen, destino);
    }

    public static ControlDeMovimientos movTableroATablero(int origen, int destino) {
        return new ControlDeMovimientos("TT", origen, destino);
    }

    public static ControlDeMovimientos movTableroAFundacion(int origen, int destino) {
        return new ControlDeMovimientos("TF", origen, destino);
    }

    public static ControlDeMovimientos movReservaATablero(int origen, int destino) {
        return new ControlDeMovimientos("RT", origen, destino);
    }

    public static ControlDeMovimientos movReservaAFundacion(int origen, int destino) {
        return new ControlDeMovimientos("RF", origen, destino);
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
            case "TR":
                carta = celdasReserva[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    pilasTablero[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "TT":
                carta = pilasTablero[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    pilasTablero[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "TF":
                carta = pilasFundacion[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    pilasTablero[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "RT":
                carta = pilasTablero[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    celdasReserva[mov.origenIndice].agregarCarta(carta);
                }
                break;

            case "RF":
                carta = pilasFundacion[mov.destinoIndice].sacarCarta();
                if (carta != null) {
                    celdasReserva[mov.origenIndice].agregarCarta(carta);
                }
                break;
        }

        return true;
    }
}