package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agárimo
 */
public class SocketCon implements Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean run;

    private Request request;
    private Response response;

    public SocketCon(Socket socket) {
        this.run = true;
        this.socket = socket;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SocketCon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconectar() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketCon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        System.out.println("SERVER----CLIENTE ACEPTADO");

        while (run) {
            try {
                request = (Request) in.readObject();

                if (processRequest()) {
                    out.writeObject(this.response);
                } else {
                    String str = "ERROR";
                    List list = new ArrayList();
                    list.add("ERROR WHILE PROCESSING REQUEST");

                    response = new Response();
                    response.setStatus(str);
                    response.setParametros(list);
                    out.writeObject(response);
                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SocketCon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        desconectar();
        System.out.println("SERVER----CLIENTE DESCONECTADO");
    }

    private boolean processRequest() {
        response = new Response();

        if (this.request.getTipo().equalsIgnoreCase("END")) {
            run = false;
            response.setStatus("DONE");
            response.setParametros(null);
        } else {
            response.setStatus("OK");
            List aux = new ArrayList();
            aux.add("asdf");
            aux.add(112);
            response.setParametros(aux);
        }

        return true;
    }

}
