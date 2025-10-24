package Juego;

public class DragInfo {
    public static final int ORIGEN_TABLERO = 0;
    public static final int ORIGEN_RESERVA = 1;

    public int origen;
    public int indice;

    public static String encode(DragInfo d) {
        return d.origen + ";" + d.indice;
    }

    public static DragInfo decode(String s) {
        String[] p = s.split(";");
        DragInfo d = new DragInfo();
        d.origen = Integer.parseInt(p[0]);
        d.indice = Integer.parseInt(p[1]);
        return d;
    }
}