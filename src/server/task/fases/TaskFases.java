package server.task.fases;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import server.task.Tarea;
import socket.enty.ModeloTarea;


/**
 *
 * @author Agárimo
 */
public class TaskFases extends Tarea implements Runnable {
    
    public static String dbName="";

    private LocalDate fecha;

    public TaskFases(ModeloTarea tarea) {
        super(tarea);

        if (tarea.getParametros() != null) {
            this.fecha = LocalDate.parse(tarea.getParametros());
            System.out.println(this.fecha.format(DateTimeFormatter.ISO_DATE));
        } else {
            this.fecha = null;
        }
    }

    @Override
    public void run() {
        init();
        fases();
        end();
    }

    private void init() {
        if (this.fecha == null) {
            this.fecha = LocalDate.now();
        }

        Thread.currentThread().setName("TaskFases Thread");
        setTitulo("FASES");
        setMensaje("Iniciando");
        setPorcentaje(0, 0);
        tarea.setParametros(this.fecha.format(DateTimeFormatter.ISO_DATE));
    }

    private void fases() {

        Boletin aux;
        Fases fs = new Fases(fecha);
        List list = fs.getBoletines();

        for (int i = 0; i < list.size(); i++) {
            final int contador = i;
            final int total = list.size();
            Platform.runLater(() -> {
                int contadour = contador + 1;
                double counter = contador + 1;
                double toutal = total;
                setMensaje("COMPROBANDO FASE " + contadour + " de " + total);
                setPorcentaje(counter,toutal);
            });
            aux = (Boletin) list.get(i);
            fs.run(aux);
        }

        
    }
    
    
    private void end(){
        setMensaje("Finalizando");
        setPorcentaje(1,1);
    }

}
