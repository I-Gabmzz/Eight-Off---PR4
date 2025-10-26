package Juego;

public class MovimientoPosible {
    public static final int TIPO_ORIGEN_TABLERO = 0;
    public static final int TIPO_ORIGEN_RESERVA = 1;
    public static final int TIPO_DESTINO_TABLERO = 0;
    public static final int TIPO_DESTINO_RESERVA = 1;
    public static final int TIPO_DESTINO_FUNDACION = 2;

    public int origenTipo;
    public int origenIndice;
    public int destinoTipo;
    public int destinoIndice;
    public int cantidad;

    public MovimientoPosible(int ot, int oi, int dt, int di, int cantidad) {
        this.origenTipo = ot;
        this.origenIndice = oi;
        this.destinoTipo = dt;
        this.destinoIndice = di;
        this.cantidad = cantidad;
    }

    public MovimientoPosible(int ot, int oi, int dt, int di) {
        this(ot, oi, dt, di, 1);
    }
}