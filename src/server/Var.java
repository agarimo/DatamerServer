package server;

import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Conexion;

/**
 *
 * @author Agárimo
 */
public class Var {

    public static Conexion con;
    public static String dbName = "server";

    public static void initVar() {
        initVarDriver();
        initConnection();
    }

    private static void initVarDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Var.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initConnection() {
        con = new Conexion();
        con.setDireccion("192.168.1.40");
        con.setPuerto("3306");
        con.setUsuario("admin");
        con.setPass("IkuinenK@@m.s84");
    }

}
