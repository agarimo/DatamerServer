package server.task;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Var;
import socket.enty.ModeloTarea;
import sql.Sql;

/**
 *
 * @author Agárimo
 */
public class Tarea implements Runnable {

    protected String titulo;
    protected String mensaje;
    protected double porcentaje;

    protected double val;
    protected String status;

    protected Sql bd;
    protected ModeloTarea tarea;

    public Tarea(ModeloTarea tarea) {
        this.tarea = tarea;
        this.tarea.setFechaInicio(LocalDateTime.now());
    }

    public ModeloTarea getModeloTarea() {
        return tarea;
    }

    @Override
    public void run() {

    }

    public void setTitulo(String titulo) {
        tarea.setTitulo(titulo);
    }

    public void setMensaje(String mensaje) {
        tarea.setProgreso(mensaje);
    }

    public void setPorcentaje(double pos, double total) {
        double res = (pos * 100) / total;
        int por = (int) res;

        if (por == 0) {
            tarea.setPorcentaje("-");
        } else {
            tarea.setPorcentaje(por + " %");
        }
    }

    protected boolean conectar() {
        try {
            bd = new Sql(Var.con);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    protected boolean desconectar() {
        try {
            bd.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.tarea.getTipo());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tarea other = (Tarea) obj;
        return Objects.equals(this.tarea.getTipo(), other.tarea.getTipo());
    }

}
