/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.download.Publicacion;
import sql.Sql;
import util.Dates;

/**
 *
 * @author Agárimo
 */
public class Query extends sql.Query {
    
    
    public static List<Publicacion> listaPublicacion(String query) {

        List<Publicacion> list = new ArrayList();
        Publicacion aux;

        try {
            bd = new Sql(Var.con);
            rs = bd.ejecutarQueryRs(query);

            while (rs.next()) {
                aux = new Publicacion();
                aux.setId(rs.getInt("id"));
                aux.setFecha(Dates.asLocalDate(rs.getDate("fecha")));
                aux.setCodigo(rs.getString("codigo"));
                aux.setEntidad(rs.getString("entidad"));
                aux.setOrigen(rs.getString("origen"));
                aux.setDescripcion(rs.getString("descripcion"));
                aux.setDatos(rs.getString("datos"));
                aux.setLink(rs.getString("link"));
                aux.setCve(rs.getString("cve"));
                aux.setSelected(rs.getBoolean("selected"));
                aux.setStatus(rs.getString("status"));
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
