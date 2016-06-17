package server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Var;
import socket.enty.Request;
import socket.enty.Response;
import socket.enty.ServerRequest;
import socket.enty.ServerResponse;

/**
 *
 * @author Agárimo
 */
public class SkServer implements Runnable {

    private ServerSocket servidor;
    Socket socket;
    private final int puerto;
    private boolean run;

    public SkServer(int puerto) {
        this.puerto = puerto;
        this.run = true;
    }

    public void shutdown() {
        run = false;
        disconnect();
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SkServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean disconnect() {
        Request request = new Request(ServerRequest.DISCONECT);
        Response sr;
        ClientSocket cs = new ClientSocket();
        cs.conect();
        sr = cs.sendRequest(request);
        System.out.println(sr.getResponse());

        return sr.getResponse().equals(ServerResponse.DISCONECTED);
    }

    public boolean isRunning() {
        return this.run;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("SkServer Thread");
        try {
            servidor = new ServerSocket(puerto);

            while (run) {
                socket = servidor.accept();
                SocketCon sc = new SocketCon(socket);
                Var.tasker.getClientExecutor().execute(sc);
            }

            servidor.close();

        } catch (SocketException ex) {
            run = false;
            Logger.getLogger(SkServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            run = false;
            Logger.getLogger(SkServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
