package server;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.task.TaskDownload;

/**
 *
 * @author agari
 */
public class Server extends Application {

    @Override
    public void init() {
        Var.initVar();
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

        LocalDate fecha = LocalDate.now();
//        fecha=fecha.plusDays(1);

        TaskDownload task = new TaskDownload(fecha);
        new Thread(task).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void test() {
        Var.initVar();

        System.exit(0);
    }
}
