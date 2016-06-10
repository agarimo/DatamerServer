/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.UI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author agari
 */
public class WinC implements Initializable {
    
    @FXML
    private TableView tabla;
    
    @FXML
    private TableColumn tareaCL;
    
    @FXML
    private TableColumn propietarioCL;
    
    @FXML
    private TableColumn estadoCL;
    
    @FXML
    private TableColumn porcentajeCL;
    
    @FXML
    private TextField tfServer;
    
    @FXML
    private TextField tfTasker;
    
    @FXML
    private MenuItem miCerrar;
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        // TODO
    }    
    
    public void initTable(){
        
    }
    
}
