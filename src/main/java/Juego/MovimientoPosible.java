package Juego;

// Se anuncia la creacion de la clase que encapsula la informacion de un movimiento
public class MovimientoPosible {
    // Constantes para definir el tipo de origen del movimiento
    public static final int TIPO_ORIGEN_TABLERO = 0;
    public static final int TIPO_ORIGEN_RESERVA = 1;

    // Constantes para definir el tipo de destino del movimiento
    public static final int TIPO_DESTINO_TABLERO = 0;
    public static final int TIPO_DESTINO_RESERVA = 1;
    public static final int TIPO_DESTINO_FUNDACION = 2;

    public int origenTipo; // Tipo de origen
    public int origenIndice; // Indice del origen
    public int destinoTipo; // Tipo de destino
    public int destinoIndice; // Indice del destino
    public int cantidad; // Cantidad de cartas a mover

    // Metodo constructor principal que inicializa todos los atributos del movimiento
    public MovimientoPosible(int ot, int oi, int dt, int di, int cantidad) {
        this.origenTipo = ot;
        this.origenIndice = oi;
        this.destinoTipo = dt;
        this.destinoIndice = di;
        this.cantidad = cantidad;
    }

    // Metodo constructor para movimientos de una sola carta
    public MovimientoPosible(int ot, int oi, int dt, int di) {
        this(ot, oi, dt, di, 1);
    }
}