package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.socket.SkServer;
import server.task.Tasker;
import sql.Conexion;

/**
 *
 * @author Agárimo
 */
public class Var {

    private static Properties config;
    
    public static File fileSystem;

    public static Conexion con;
    public static String dbName;

    public static SkServer server;
    public static int serverPort;
    
    public static Tasker tasker;
    public static int horaExec;
    public static int minExec;
    public static int delayExec;

    public static void initVar() {
        loadConfig();
        initVarDriver();
        initConnection();
        
        fileSystem = new File("data");
        fileSystem.mkdirs();
        

        dbName = config.getProperty("dbName");
        serverPort = Integer.parseInt(config.getProperty("server_port"));
        horaExec = Integer.parseInt(config.getProperty("hora_exec"));
        minExec = Integer.parseInt(config.getProperty("min_exec"));
        delayExec = Integer.parseInt(config.getProperty("delay_exec"));
        
        
        
    }

    private static void initVarDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Var.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initConnection() {
        con = new Conexion();
        con.setDireccion(config.getProperty("con_host"));
        con.setPuerto(config.getProperty("con_port"));
        con.setUsuario(config.getProperty("con_user"));
        con.setPass(config.getProperty("con_pass"));
    }

    private static void loadConfig() {
        try {
            config = new Properties();
            InputStream in = new FileInputStream("config.xml");
            config.loadFromXML(in);
            in.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveConfig() {
        OutputStream salida = null;

        try {
            salida = new FileOutputStream("config.xml");

            config.storeToXML(salida, "Archivo de propiedades XML de Datamer_server");

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
