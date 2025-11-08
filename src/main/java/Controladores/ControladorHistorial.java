package Controladores;

import Juego.ControlDeMovimientos;
import Juego.EightOffGame;
import Juego.Foundation;
import Juego.Reserva;
import Juego.Tablero;
import Logica.NodoDoble;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

// Controlador para la ventana de los movimientos del historial
public class ControladorHistorial {

    @FXML private ListView<String> listaMovimientosView;
    @FXML private Button botonRetroceder;
    @FXML private Button botonAvanzar;
    @FXML private Button botonAplicar;
    @FXML private Button botonCerrar;

    // Referencias al juego principal
    private ControladorTablero controladorTablero;
    private Tablero[] pilasTablero;
    private Reserva[] celdasReserva;
    private Foundation[] pilasFundacion;

    private final ObservableList<String> movimientosItems = FXCollections.observableArrayList();

    // Metodo que se llama al iniciar la ventana
    public void initialize() {
        listaMovimientosView.setItems(movimientosItems);
        listaMovimientosView.setMouseTransparent(true);
        listaMovimientosView.setFocusTraversable(false);
    }

    // Metodo para pasar los datos del juego a este controlador
    public void initData(ControladorTablero controladorTablero, EightOffGame juego) {
        this.controladorTablero = controladorTablero;
        this.pilasTablero = juego.getPilasDeTablero();
        this.celdasReserva = juego.getCeldasDeReserva();
        this.pilasFundacion = juego.getPilasDeFundacion();

        ControlDeMovimientos.guardarPosicion();

        refrescarHistorial();
    }

    // Se encarga de actualizar los botones y la lista
    private void refrescarHistorial() {
        botonRetroceder.setDisable(!ControlDeMovimientos.puedeDeshacer());
        botonAvanzar.setDisable(!ControlDeMovimientos.puedeRehacer());

        movimientosItems.clear();
        NodoDoble<ControlDeMovimientos> posicionActual = ControlDeMovimientos.getPosicionActual();

        for (NodoDoble<ControlDeMovimientos> nodo : ControlDeMovimientos.getHistorialNodos()) {
            movimientosItems.add(nodo.getInfo().toString());
            if (nodo == posicionActual) {
                listaMovimientosView.getSelectionModel().select(movimientosItems.size() - 1);
                listaMovimientosView.scrollTo(movimientosItems.size() - 1);
            }
        }

        if (listaMovimientosView.getSelectionModel().getSelectedIndex() == -1) {
            listaMovimientosView.getSelectionModel().select(-1);
        }
    }

    // Maneja el boton de retroceder
    @FXML
    private void retroceder(ActionEvent event) {
        ControlDeMovimientos.deshacer(pilasTablero, celdasReserva, pilasFundacion);
        controladorTablero.dibujar();
        refrescarHistorial();
    }

    // Maneja el boton de avanzar
    @FXML
    private void avanzar(ActionEvent event) {
        ControlDeMovimientos.rehacer(pilasTablero, celdasReserva, pilasFundacion);
        controladorTablero.dibujar();
        refrescarHistorial();
    }

    // Maneja el boton de aplicar cambios
    @FXML
    private void aplicarCambios(ActionEvent event) {
        ControlDeMovimientos.aplicarPosicion();
        Stage stage = (Stage) botonCerrar.getScene().getWindow();
        stage.close();
    }

    // Maneja el boton de cerrar sin aplicar
    @FXML
    private void cerrarSinCambios(ActionEvent event) {
        ControlDeMovimientos.restaurarPosicion(pilasTablero, celdasReserva, pilasFundacion);
        controladorTablero.dibujar();
        Stage stage = (Stage) botonCerrar.getScene().getWindow();
        stage.close();
    }
}