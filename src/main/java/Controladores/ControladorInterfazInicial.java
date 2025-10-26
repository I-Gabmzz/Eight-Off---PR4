package Controladores;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

// Se anuncia la creacion de la clase que maneja la pantalla inicial de la aplicacion
public class ControladorInterfazInicial {
    // Se declaran los atributos de la clase
    @FXML private ImageView fondo; // Imagen de fondo de la interfaz
    @FXML private Button botonJugar, botonCreditos, botonSalir; // Botones principales

    // Estilos para los botones en estado normal, con hover y presionados
    private static final String BOTONES_NORMALES =
            "-fx-background-color: rgba(30,30,30,0.35);" +
                    "-fx-text-fill: white;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;" +
                    "-fx-font-weight: 700;" +
                    "-fx-focus-color: transparent;" +
                    "-fx-faint-focus-color: transparent;";

    private static final String BOTONES_CON_HOVER =
            "-fx-background-color: rgba(30,30,30,0.55);" +
                    "-fx-text-fill: #2196F3;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;" +
                    "-fx-font-weight: 700;" +
                    "-fx-focus-color: transparent;" +
                    "-fx-faint-focus-color: transparent;";

    private static final String BOTONES_PRESIONADOS =
            "-fx-background-color: rgba(30,30,30,0.70);" +
                    "-fx-text-fill: #64B5F6;" +
                    "-fx-border-color: #ffffff;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;" +
                    "-fx-font-weight: 700;" +
                    "-fx-focus-color: transparent;" +
                    "-fx-faint-focus-color: transparent;";

    // Metodo que coloca a los botones con un estilo prestablecido como normal
    private void aplicarNormal(Button b, DropShadow sombra) {
        b.setStyle(BOTONES_NORMALES);
        b.setTextFill(Color.WHITE);
        b.setEffect(sombra);
    }

    // Este metodo simplemente le da estilo a un botón y configura sus efectos visuales de hover y clic
    private void darEstiloAlBoton(Button boton) {
        boton.setFocusTraversable(false);
        boton.setDefaultButton(false);
        boton.setCancelButton(false);

        DropShadow sombraNormal = new DropShadow();
        sombraNormal.setRadius(10);
        sombraNormal.setOffsetY(3);
        sombraNormal.setColor(Color.rgb(20, 60, 120, 0.35));

        DropShadow sombraHover = new DropShadow();
        sombraHover.setRadius(18);
        sombraHover.setOffsetY(6);
        sombraHover.setColor(Color.rgb(20, 60, 120, 0.60));

        DropShadow sombraPresionado = new DropShadow();
        sombraPresionado.setRadius(8);
        sombraPresionado.setOffsetY(2);
        sombraPresionado.setColor(Color.rgb(15, 45, 90, 0.75));

        aplicarNormal(boton, sombraNormal);

        boton.setOnMouseEntered(e -> {
            boton.setStyle(BOTONES_CON_HOVER);
            boton.setTextFill(Color.web("#2196F3"));
            boton.setEffect(sombraHover);
            boton.setCursor(javafx.scene.Cursor.HAND);
        });
        boton.setOnMouseExited(e -> {
            aplicarNormal(boton, sombraNormal);
        });
        boton.setOnMousePressed(e -> {
            boton.setStyle(BOTONES_PRESIONADOS);
            boton.setTextFill(Color.web("#64B5F6"));
            boton.setEffect(sombraPresionado);
        });
        boton.setOnMouseReleased(e -> {
            if (boton.isHover()) {
                boton.setStyle(BOTONES_CON_HOVER);
                boton.setTextFill(Color.web("#2196F3"));
                boton.setEffect(sombraHover);
            } else {
                aplicarNormal(boton, sombraNormal);
            }
        });
    }

    // Este metodo inicializador lo que hace es ajustar el fondo al tamaño de la ventana y aplica estilos a los botones
    @FXML
    private void initialize() {
        fondo.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) {
                fondo.fitWidthProperty().bind(scene.widthProperty());
                fondo.fitHeightProperty().bind(scene.heightProperty());
            }
        });
        darEstiloAlBoton(botonJugar);
        darEstiloAlBoton(botonCreditos);
        darEstiloAlBoton(botonSalir);
    }

    // Abre la ventana del tablero al presionar "Jugar"
    @FXML
    private void alDarSimular(ActionEvent e) throws IOException {
        Parent raizTablero = FXMLLoader.load(getClass().getResource("/GUI/Tableroo.fxml"));
        Stage venActual  = (Stage) botonJugar.getScene().getWindow();
        Stage venModo = new Stage();
        venModo.setTitle("Eight Off Tablero");
        venModo.setFullScreenExitHint("");
        venModo.setScene(new Scene(raizTablero, venActual.getWidth(), venActual.getHeight()));
        venModo.setMaximized(venActual.isMaximized());
        venModo.setFullScreen(venActual.isFullScreen());
        venActual.close();
        venModo.show();
    }

    // Este metodo muestra una ventana emergente con los créditos del juego
    @FXML
    private void alDarCreditos(ActionEvent e) {
        var stage = (Stage) botonCreditos.getScene().getWindow();
        var alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.initOwner(stage);
        alert.initModality(javafx.stage.Modality.WINDOW_MODAL);
        alert.initStyle(javafx.stage.StageStyle.UTILITY);
        alert.setTitle("Créditos");
        alert.setHeaderText("Eight Off Solitaire | Créditos");
        alert.setContentText("Creador: Angel Gabriel Manjarrez Moreno \n" +
                "Matricula: 1197503 \n" +
                "Materia: Algoritmos y Estructuras de datos \n" +
                "Versión: 24 de octubre de 2025");
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    }

    // Cierra la aplicación al dar clic en "Salir"
    @FXML
    private void alDarSalir(ActionEvent e) {
        Stage stage = (Stage) botonSalir.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
}