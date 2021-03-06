package server.task;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Query;
import server.Var;
import server.download.Publicacion;
import server.download.Status;
import socket.enty.ModeloTarea;

/**
 *
 * @author Agárimo
 */
public class TaskClasificacion extends Tarea {

    private List<Publicacion> publicacion;
    private List<String> entidad;
    private List<String> seleccion;
    private List<String> descarte;

    private String queryPublicacion = "SELECT * FROM " + Var.dbName + ".publicacion WHERE selected IS null";
    private String queryEntidad = "SELECT entidad FROM " + Var.dbName + ".entidad_descarte";
    private String querySeleccion = "SELECT texto FROM " + Var.dbName + ".texto_seleccion";
    private String queryDescarte = "SELECT texto FROM " + Var.dbName + ".texto_descarte";

    public TaskClasificacion(ModeloTarea tarea) {
        super(tarea);
    }

    @Override
    public void run() {
        init();
        initTarea();
        initData();
        
        runSeleccion();
        runEntidad();
        runDescarte();

        endTarea();
    }

    private void init() {
        Thread.currentThread().setName("TaskClasificacion Thread");
        setTitulo("CLASIFICACION");
        setPorcentaje(0, 0);
        setMensaje("Iniciando");
    }

    private void initData() {
        setMensaje("Cargando Datos");
        try {
            entidad = bd.getStringList(queryEntidad);
            seleccion = bd.getStringList(querySeleccion);
            descarte = bd.getStringList(queryDescarte);
            publicacion = Query.listaPublicacion(queryPublicacion);
        } catch (SQLException ex) {
            Logger.getLogger(TaskClasificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void runEntidad() {
        setTitulo("CLASF Entidad");
        setMensaje("Iniciando");
        List<Publicacion> ex = new ArrayList();
        val = 1;

        publicacion.stream().forEach((aux) -> {
            status = val + " de " + publicacion.size();
            status = status.replace(".0", "");
            setPorcentaje(val, publicacion.size());
            setMensaje("Comprobando " + status);

            if (entidad.contains(aux.getEntidad())) {
                aux.setSelected(false);
                aux.setStatus(Status.SOURCE);

                try {
                    bd.ejecutar(aux.SQLEditarStatus());
                } catch (SQLException ex1) {
                    Logger.getLogger(TaskClasificacion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } else {
                ex.add(aux);
            }
            val++;
        });

        publicacion.clear();
        publicacion.addAll(ex);

    }

    private void runSeleccion() {
        setTitulo("CLASF Selección");
        setMensaje("Iniciando");
        List<Publicacion> ex = new ArrayList();
        val = 1;

        publicacion.stream().forEach((aux) -> {
            status = val + " de " + publicacion.size();
            status = status.replace(".0", "");
            setPorcentaje(val, publicacion.size());
            setMensaje("Comprobando " + status);

            if (buscar(seleccion, aux.getDatos())) {
                aux.setSelected(true);
                aux.setStatus(Status.APP);

                try {
                    bd.ejecutar(aux.SQLEditarStatus());
                } catch (SQLException ex1) {
                    Logger.getLogger(TaskClasificacion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } else {
                ex.add(aux);
            }
            val++;
        });

        publicacion.clear();
        publicacion.addAll(ex);
    }

    private void runDescarte() {
        setTitulo("CLASF Descarte");
        setMensaje("Iniciando");
        List<Publicacion> ex = new ArrayList();
        val = 1;

        publicacion.stream().forEach((aux) -> {
            status = val + " de " + publicacion.size();
            status = status.replace(".0", "");
            setPorcentaje(val, publicacion.size());
            setMensaje("Comprobando " + status);

            if (buscar(descarte, aux.getDatos())) {
                aux.setSelected(false);
                aux.setStatus(Status.APP);

                try {
                    bd.ejecutar(aux.SQLEditarStatus());
                } catch (SQLException ex1) {
                    Logger.getLogger(TaskClasificacion.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } else {
                ex.add(aux);
            }
            val++;
        });

        publicacion.clear();
        publicacion.addAll(ex);
    }

    private boolean buscar(List<String> lista, String str) {
        String aux;
        Iterator<String> it = lista.iterator();

        while (it.hasNext()) {
            aux = it.next();

            if (str.contains(aux)) {
                return true;
            }
        }

        return false;
    }

}
