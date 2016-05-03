package server.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.socket.Request;
import server.socket.Response;

/**
 *
 * @author Agárimo
 */
public class ClientSocket extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean run;

    private final Request request;
    private Response response;

    public ClientSocket(Request request) {
        this.request = request;
        this.run = true;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("127.0.0.1", 10987);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (run) {
                System.out.println("Enviando request");
                System.out.println(request);
                out.writeObject(request);
                System.out.println("sent");

                System.out.println("Recibiendo request");
                response = (Response) in.readObject();
                System.out.println(response);
                System.out.println("recibida");

                if (response.getStatus().equalsIgnoreCase("DONE") || response.getStatus().equalsIgnoreCase("ERROR")) {
                    run = false;
                    break;
                }
                
            }

            System.out.println("desconectando cliente");
            in.close();
            out.close();
            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
