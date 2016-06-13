/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import socket.enty.ModeloTarea;

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
        initTable();
        // TODO
    }

    public void initTable() {
        tareaCL.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        propietarioCL.setCellValueFactory(new PropertyValueFactory<>("propietario"));
        porcentajeCL.setCellValueFactory(new PropertyValueFactory<>("porcentaje"));
        estadoCL.setCellValueFactory(new PropertyValueFactory<>("progreso"));
        estadoCL.setCellFactory((TableColumn<ModeloTarea, String> arg0) -> new TableCell<ModeloTarea, String>() {
            private Text text;

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
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
    

}
