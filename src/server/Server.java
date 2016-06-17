package server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author agari
 */
public class Server extends Application {

    private static Stage stage;
    private Parent root;

    @Override
    public void init() throws IOException {
        Var.initVar();
        root = FXMLLoader.load(getClass().getResource("/server/UI/Win.fxml"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage st) throws Exception {

        Scene scene = new Scene(root);
        stage = st;

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.setTitle("DatamerServer 0.1");
        stage.show();

        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
            minimize();
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void shutdown() {
        stage.hide();
        Var.shutdown();
        Platform.exit();
    }
    
    private static void minimize(){
        stage.setIconified(true);
    }
}
