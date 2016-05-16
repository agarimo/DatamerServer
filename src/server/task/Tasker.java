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
import socket.enty.ModeloTarea;
import socket.enty.ServerTask;

/**
 *
 * @author Agárimo
 */
public class Tasker {

    private int count;
    private long initDelay;
    private long delay;
    private ScheduledExecutorService ses;
    private List<Tarea> running_task;

    public Tasker() {
        count = 1;
        ses = Executors.newScheduledThreadPool(1);
        running_task = new ArrayList();
    }

    public void initRutina() {
        ModeloTarea mt = new ModeloTarea();
        mt.setId(-1);
        mt.setPropietario("SERVER");

//        long ahora = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        long futuro = LocalDateTime.now().plusDays(1).withHour(Var.horaExec).withMinute(Var.minExec).toEpochSecond(ZoneOffset.UTC);
//        initDelay = futuro - ahora;
        initDelay = 1;
        delay = Var.delayExec;

        mt.setTipoTarea(ServerTask.BOE);
        TaskDownload descarga = new TaskDownload(mt);

        ses.scheduleAtFixedRate(descarga, initDelay, delay, TimeUnit.SECONDS);

//        mt.setTipoTarea(ServerTask.BOE_CLASIFICACION);
//        TaskClasificacion clasificacion = new TaskClasificacion(mt);
//        initDelay = initDelay + 900;
//
//        ses.scheduleAtFixedRate(clasificacion, initDelay, delay, TimeUnit.SECONDS);
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
                answer = false;
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
