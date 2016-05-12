package server.task;

import socket.enty.ModeloTarea;

/**
 *
 * @author Agárimo
 */
public class TaskClasificacion extends Tarea{

    public TaskClasificacion(ModeloTarea tarea) {
        super(tarea);
    }

    @Override
    protected Object call() throws Exception {
        System.out.println("Me ejecutoooo CLASIFICACION");
        
        return null;
    }
   

}
