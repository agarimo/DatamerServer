/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.UI;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import server.Var;
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
    
    @FXML
    private ComboBox cbRefresh;

    private ObservableList<ModeloTarea> tareas;
    private ObservableList<String> comboBox;
    private long refreshTime;
    private boolean keepRefresh;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initVar();
        initStatus();
        initTable();
        initRefresh();
    }
    
    public void initVar(){
        keepRefresh=true;
        refreshTime=5000;
        
        comboBox = FXCollections.observableArrayList();
        cbRefresh.setItems(comboBox);
        comboBox.add("1 segundo");
        comboBox.add("5 segundos");
        comboBox.add("10 segundos");
        comboBox.add("15 segundos");
        comboBox.add("30 segundos");
        
        cbRefresh.getSelectionModel().select("5 segundos");
        
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

    public void initRefresh() {
        Thread a = new Thread(() -> {

            while (keepRefresh) {
                List<ModeloTarea> aux = Var.tasker.getStatus();

                Platform.runLater(() -> {
                    tareas.clear();
                    tareas.addAll(aux);
                    initStatus();
                });

                try {
                    Thread.sleep(refreshTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WinC.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        a.start();
    }
    
    public void initStatus(){
        
        
        if(Var.server.isRunning()){
            tfServer.setStyle("-fx-text-fill: green;"
                    + "-fx-background-color: black");
        }else{
            tfServer.setStyle("-fx-text-fill: red;"
                    + "-fx-background-color: black");
        }
        
        if(Var.tasker.isRunning()){
            tfTasker.setStyle("-fx-text-fill: green;"
                    + "-fx-background-color: black");
        }else{
            tfTasker.setStyle("-fx-text-fill: red;"
                    + "-fx-background-color: black");
        }
    }

}
