package server.task;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import server.Var;
import socket.enty.ModelTask;
import socket.enty.ModeloTarea;
import socket.enty.ServerTask;

/**
 *
 * @author Agárimo
 */
public class Tasker {

//    private static int idCount;
    private boolean run;
    private ScheduledExecutorService scheduledExecutor;
    private List<Tarea> running_task;

    public Tasker() {
//        idCount = 1;
        run = false;
        scheduledExecutor = Executors.newScheduledThreadPool(6);
        running_task = new ArrayList();
        run = true;
    }

    public boolean isRunning() {
        return this.run;
    }

    public ScheduledExecutorService getScheduledExecutor() {
        return scheduledExecutor;
    }

    public void sheduledTask() {
        ModeloTarea mt = new ModeloTarea();
        mt.setPropietario("SCHEDULER");
        mt.setTipoTarea(ServerTask.BOE);
        TaskDownload task = new TaskDownload(mt);
        long initDelay = computeDelay();

        scheduledExecutor.scheduleAtFixedRate(task, initDelay, Var.delayExec, TimeUnit.SECONDS);
    }

    private long computeDelay() {
        long ahora = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        long futuro = LocalDateTime.now().plusDays(1).withHour(Var.horaExec).withMinute(Var.minExec).toEpochSecond(ZoneOffset.UTC);
        return futuro - ahora;
    }

    public void shutdown() {
        scheduledExecutor.shutdownNow();
        run = false;
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

    public synchronized List<ModelTask> getStatus() {
        List<ModelTask> list = new ArrayList();

        running_task.stream().forEach((aux) -> {
            list.add(new ModelTask(aux.getModeloTarea()));
        });

        return list;
    }
    
    public synchronized List<ModeloTarea> getStatusLocal() {
        List<ModeloTarea> list = new ArrayList();

        running_task.stream().forEach((aux) -> {
            list.add(aux.getModeloTarea());
        });

        return list;
    }

    public synchronized void addTask(Tarea aux) {
        running_task.add(aux);
    }

    public synchronized void removeTask(Tarea aux) {
        running_task.remove(aux);
    }

    private void runBoe(ModeloTarea tarea) {
        TaskDownload task = new TaskDownload(tarea);
        scheduledExecutor.execute(task);
    }

    private void runBoeClasificacion(ModeloTarea tarea) {
        TaskClasificacion task = new TaskClasificacion(tarea);
        scheduledExecutor.execute(task);
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
