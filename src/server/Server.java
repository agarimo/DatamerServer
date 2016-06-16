package server;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.socket.SkServer;
import server.task.Tasker;

/**
 *
 * @author agari
 */
public class Server extends Application {

    private static Stage stage;

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
    public void start(Stage st) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/server/UI/Win.fxml"));

        Scene scene = new Scene(root);

        stage = st;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();

        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            System.out.println("Se quiere cerrar el jodío!");
            shutdown();
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private static void initServer() {
        Var.server = new SkServer(Var.serverPort);
        new Thread(Var.server).start();
    }

    private static void initTasker() {
        Var.tasker = new Tasker();
        Var.tasker.initRutina();
    }

    private static void shutdown() {
        stage.hide();
        Var.tasker.shutdown();
        Var.server.shutdown();
        System.exit(0);

    }
}
