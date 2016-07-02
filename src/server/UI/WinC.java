package server.UI;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import server.Var;
import socket.enty.ModeloTarea;
import socket.enty.ServerTask;

/**
 *
 * @author agari
 */
public class WinC implements Initializable {

    @FXML
    private TableView<ModeloTarea> tabla;

    @FXML
    private TableColumn<ModeloTarea, String> tareaCL;

    @FXML
    private TableColumn<ModeloTarea, String> propietarioCL;

    @FXML
    private TableColumn<ModeloTarea, String> estadoCL;

    @FXML
    private TableColumn<ModeloTarea, String> porcentajeCL;

    @FXML
    private TextField tfServer;

    @FXML
    private TextField tfTasker;

    @FXML
    private MenuItem miCerrar;

    private ObservableList<ModeloTarea> tareas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initStatus();
        initTable();
        initRefresh();
    }

    public void initTable() {
        tareaCL.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tareaCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER);
                if (!isEmpty()) {
                    text = new Text(item);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });
        propietarioCL.setCellValueFactory(new PropertyValueFactory<>("propietario"));
        propietarioCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER);
                if (!isEmpty()) {
                    text = new Text(item);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });
        porcentajeCL.setCellValueFactory(new PropertyValueFactory<>("porcentaje"));
        porcentajeCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER);
                if (!isEmpty()) {
                    text = new Text(item);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });
        estadoCL.setCellValueFactory(new PropertyValueFactory<>("progreso"));
        estadoCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                super.setAlignment(Pos.CENTER_LEFT);
                if (!isEmpty()) {
                    text = new Text(item);
                    text.setWrappingWidth(estadoCL.getWidth() - 10);
                    this.setWrapText(true);
                    setGraphic(text);
                } else {
                    text = new Text("");
                    setGraphic(text);
                }
            }
        });

        tareas = FXCollections.observableArrayList();
        tabla.setItems(tareas);
    }

    public void initRefresh() {
        Runnable refresh = () -> {
            Thread.currentThread().setName("Refresh Thread");
            while (Var.keepRefresh) {
                List<ModeloTarea> aux = Var.tasker.getStatusLocal();

                Platform.runLater(() -> {
                    tareas.clear();
                    tareas.addAll(aux);
                    initStatus();
                });

                try {
                    Thread.sleep(Var.refreshTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Var.tasker.getScheduledExecutor().execute(refresh);
    }

    public void initStatus() {
        if (Var.server.isRunning()) {
            tfServer.setStyle("-fx-text-fill: green;"
                    + "-fx-background-color: black");
        } else {
            tfServer.setStyle("-fx-text-fill: red;"
                    + "-fx-background-color: black");
        }

        if (Var.tasker.isRunning()) {
            tfTasker.setStyle("-fx-text-fill: green;"
                    + "-fx-background-color: black");
        } else {
            tfTasker.setStyle("-fx-text-fill: red;"
                    + "-fx-background-color: black");
        }
    }

    @FXML
    void runClasificacionDate(ActionEvent event) {
            
    }

    @FXML
    void runClasificacion(ActionEvent event) {
        ModeloTarea mt = new ModeloTarea();
        mt.setPropietario("SERVER");
        mt.setTipoTarea(ServerTask.BOE_CLASIFICACION);

        Var.tasker.runTask(mt);
    }

    @FXML
    void runDownload(ActionEvent event) {
        ModeloTarea mt = new ModeloTarea();
        mt.setPropietario("SERVER");
        mt.setTipoTarea(ServerTask.BOE);

        Var.tasker.runTask(mt);
    }

    @FXML
    void close(ActionEvent event) {
        server.Server.shutdown();
    }

}
