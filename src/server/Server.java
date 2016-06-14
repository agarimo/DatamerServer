package server;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static server.Var.tasker;
import server.socket.SkServer;
import server.task.Tasker;
import sql.Sql;
import util.Varios;

/**
 *
 * @author agari
 */
public class Server extends Application {
    
    private static SkServer server;

    @Override
    public void init() {
        Var.initVar();
        initServer();
        initTasker();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/server/UI/Win.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
//        test();
        
    }
    
    private static void initServer(){
        System.out.println("Iniciando SERVER");
        server = new SkServer(Var.serverPort);
        new Thread(server).start();
    }
    
    private static void initTasker(){
        System.out.println("Iniciando TASKER");
        tasker = new Tasker();
        tasker.initRutina();
//        new Thread(tasker).start();
    }

    public static void test() {
        Var.initVar();
//        initServer();
        initTasker();

//        ClientSocket client = new ClientSocket();
//
//        client.conect();

//        Request request = new Request(ServerRequest.RUN_TASK);
//        request.getParametros().add(ServerTask.FASES);
//        request.getParametros().add(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
//
//        System.out.println("CLIENT--Envia peticion");
//        Response res = client.sendRequest(request);
//        System.out.println("CLIENT--Lee Respuesta");
//        System.out.println(res);
//        client.disconect();
    }
}
