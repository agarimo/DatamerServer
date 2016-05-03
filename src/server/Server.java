package server;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.client.ClientSocket;
import server.socket.Request;
import server.socket.SkServer;
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
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        launch(args);
        test();
    }

    public static void test() {
        Var.initVar();

        SkServer server = new SkServer(10987);
        new Thread(server).start();
        
        Request request = new Request();
        request.setTipo("END");
        List aux = new ArrayList();
        aux.add("Un parametro");
        aux.add(123);
        request.setParametros(aux);
        
        ClientSocket client = new ClientSocket(request);
        new Thread(client).start();
            
        
    }
}
