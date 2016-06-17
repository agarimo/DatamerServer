package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import socket.enty.Request;
import socket.enty.Response;
import socket.enty.ServerResponse;
import socket.enty.ServerTask;

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
        while (run) {
            try {
                request = (Request) in.readObject();

                if (processRequest()) {
                    out.writeObject(this.response);
                } else {
                    response = new Response(ServerResponse.ERROR);
                    response.getParametros().add("ERROR PROCESANDO EL REQUEST");
                    out.writeObject(response);
                }

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SocketCon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        desconectar();
    }

    private boolean processRequest() {

        switch (request.getRequest()) {
            case CONNECT:
                response = new Response(ServerResponse.CONECTED);
                break;
            case DISCONECT:
                run = false;
                response = new Response(ServerResponse.DISCONECTED);
                break;

            case STATUS:

                break;

            case RUN_TASK:
                ServerTask st = (ServerTask) request.getParametros().get(0);
                String param = (String) request.getParametros().get(1);
                response = new Response(ServerResponse.OK);
                response.getParametros().add(st);
                response.getParametros().add(param);
                break;
            default:
                response = new Response(ServerResponse.ERROR);
                response.getParametros().add("Error en SWITCH de processRequest()");
                break;
        }

        return true;
    }
}
