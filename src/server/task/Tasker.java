package server.task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import server.Var;
import socket.enty.ModeloTarea;
import socket.enty.ServerTask;

/**
 *
 * @author Agárimo
 */
public class Tasker implements Runnable{

    private long initDelay;
    private long delay;
    private ScheduledExecutorService ses;
    private List<Tarea> running_task;

    public Tasker() {
        ses = Executors.newSingleThreadScheduledExecutor();
        running_task = new ArrayList();
    }
    
    @Override
    public void run(){
        ModeloTarea mt;
        List<ModeloTarea> list;
        Iterator<ModeloTarea> it;
        
        while(true){
            list = this.getStatus();
            it = list.iterator();
            
            while(it.hasNext()){
                mt = it.next();
                System.out.println(mt);
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Tasker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public void initRutina() {

        ModeloTarea mt = new ModeloTarea();
        mt.setId(-1);
        mt.setPropietario("SERVER");

        long ahora = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long futuro = LocalDateTime.now().plusDays(1).withHour(Var.horaExec).withMinute(Var.minExec).toEpochSecond(ZoneOffset.UTC);
        initDelay = futuro - ahora;
//        initDelay = 1;
        delay = Var.delayExec;

        mt.setTipoTarea(ServerTask.BOE);
        TaskDownload descarga = new TaskDownload(mt);

        ses.scheduleAtFixedRate(descarga, initDelay, delay, TimeUnit.SECONDS);

        mt.setTipoTarea(ServerTask.BOE_CLASIFICACION);
        TaskClasificacion clasificacion = new TaskClasificacion(mt);
        initDelay = initDelay + 1;
//
        ses.scheduleAtFixedRate(clasificacion, initDelay, delay, TimeUnit.SECONDS);
    }

    public synchronized boolean runTask(ModeloTarea tarea) {
        boolean answer = false;

        switch (tarea.getTipo()) {
            case BOE:
                if (!isRunning(ServerTask.BOE)) {
                    runBoe(tarea);
                    answer = true;
                }
                break;
            case BOE_CLASIFICACION:
                if (!isRunning(ServerTask.BOE_CLASIFICACION)) {
                    runBoeClasificacion(tarea);
                    answer = true;
                }
                break;
            case ESTRUCTURAS:
                if (!isRunning(ServerTask.ESTRUCTURAS)) {
                    runEstructuras(tarea);
                    answer = true;
                }
                break;
            case ESTRUCTURAS_PENDIENTES:
                if (!isRunning(ServerTask.ESTRUCTURAS_PENDIENTES)) {
                    runEstructurasPendientes(tarea);
                    answer = true;
                }
                break;
            case FASES:
                if (!isRunning(ServerTask.FASES)) {
                    runFases(tarea);
                    answer = true;
                }
                break;
            default:
                break;
        }

        return answer;
    }

    public synchronized List<ModeloTarea> getStatus() {
        List<ModeloTarea> list = new ArrayList();

        running_task.stream().forEach((aux) -> {
            list.add(aux.getModeloTarea());
        });

        return list;
    }
    
    public synchronized void addTask(Tarea aux){
        running_task.add(aux);
    }
    
    public synchronized void removeTask(Tarea aux){
        running_task.remove(aux);
    }

    private void runBoe(ModeloTarea tarea) {

    }

    private void runBoeClasificacion(ModeloTarea tarea) {

    }

    private void runEstructuras(ModeloTarea tarea) {

    }

    private void runEstructurasPendientes(ModeloTarea tarea) {

    }

    private void runFases(ModeloTarea tarea) {

    }

    private boolean isRunning(ServerTask task) {
        List<Tarea> result = running_task.stream()
                .filter(p -> p.getModeloTarea().getTipo().equals(task))
                .collect(Collectors.toList());

        return !result.isEmpty();
    }

}
