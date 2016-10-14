package server.task.fases;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Var;
import sql.Sql;

/**
 *
 * @author Agarimo
 */
public class Query extends sql.Query {

    public static List<Boletin> listaBoletin(String query) {

        List<Boletin> list = new ArrayList();
        Boletin aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Boletin(rs.getInt("id"), rs.getInt("idOrigen"), rs.getInt("idBoe"),
                        rs.getString("codigo"), rs.getString("tipo"), rs.getString("fase"), rs.getInt("isFase"),
                        rs.getInt("isEstructura"), rs.getInt("idioma"));
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Fase> listaFase(String query) {
        List list = new ArrayList();
        Fase aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Fase();
                aux.setId(rs.getInt("id"));
                aux.setIdOrigen(rs.getInt("idOrigen"));
                aux.setCodigo(rs.getString("codigo"));
                aux.setTipo(rs.getInt("tipo"));
                aux.setTexto1(rs.getString("texto1"));
                aux.setTexto2(rs.getString("texto2"));
                aux.setTexto3(rs.getString("texto3"));
                aux.setPlazo(rs.getString("plazo"));
                list.add(aux);
            }
            rs.close();
            bd.close();
        } catch (SQLException ex) {
            error(ex.getMessage());
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
