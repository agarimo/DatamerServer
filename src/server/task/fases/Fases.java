package server.task.fases;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Var;
import sql.Sql;
import tools.Util;

/**
 *
 * @author Agarimo
 */
public class Fases {

    Sql bd;
    LocalDate fecha;
    List boletines;

    public Fases(LocalDate fecha) {
        this.fecha = fecha;
        this.boletines = getBol();
    }

    private List getBol() {
        return Query.listaBoletin("SELECT * FROM " + TaskFases.dbName + ".boletin where idBoe in "
                + "(SELECT id FROM " + TaskFases.dbName + ".boe where fecha=" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + ")");
    }

    public List getBoletines() {
        return this.boletines;
    }

    private void conectar() {
        try {
            bd = new Sql(Var.con);
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void desconectar() {
        try {
            bd.close();
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDatos(String codigo) {
        String str = null;

        try {
            str = bd.getString("SELECT datos FROM " + TaskFases.dbName + ".publicacion where codigo=" + Util.comillas(codigo));
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public void run(Boletin aux) {
        Fase fase;
        conectar();

        fase = compruebaFase(aux.getCodigo(), getFases(aux.getIdOrigen()));

        if (fase != null) {
            aux.setTipo(fase.getCodigo());
            aux.setFase(getBCN(aux.getIdOrigen(), aux.getIsEstructura()) + "-" + fase.toString());

            if (fase.getCodigo().equals("*DSC*")) {
                aux.setIsFase(3);
            } else {
                aux.setIsFase(2);
            }
        } else {
            aux.setFase(getBCN(aux.getIdOrigen(), aux.getIsEstructura()));
            aux.setIsFase(1);
        }

        try {
            bd.ejecutar(aux.SQLEditar());
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }
        desconectar();
    }

    private Fase compruebaFase(String codigo, List fases) {
        String datos = getDatos(codigo);
        Fase fase = null;
        Fase aux;
        Iterator it = fases.iterator();

        while (it.hasNext()) {
            aux = (Fase) it.next();

            if (aux.contiene(datos)) {
                fase = aux;
            }
        }
        return fase;
    }

    private String getBCN(int idOrigen, int estructura) {
        String str = "";

        try {
            if (estructura == -1) {
                str = "BCN1null";
            } else {
                str = bd.getString("SELECT nombre FROM " + TaskFases.dbName + ".estructura where id=" + estructura);
            }
            str = str + bd.getString("SELECT codigoUn FROM " + TaskFases.dbName + ".origen where id=" + idOrigen);
        } catch (SQLException ex) {
            Logger.getLogger(Fases.class.getName()).log(Level.SEVERE, null, ex);
        }

        return str;
    }

    private List getFases(int id) {
        return Query.listaFase("SELECT * FROM " + TaskFases.dbName + ".fase where idOrigen=" + id);
    }
    
    
}
