package server.task;

import java.time.LocalDateTime;
import javafx.concurrent.Task;
import socket.enty.ModeloTarea;

/**
 *
 * @author Agárimo
 */
public class Tarea extends Task {

    ModeloTarea tarea;
    
    public Tarea(ModeloTarea tarea){
        this.tarea=tarea;
        this.tarea.setFechaInicio(LocalDateTime.now());
    }
    
    public ModeloTarea getModeloTarea() {
        return tarea;
    }
    
    @Override
    protected Object call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
