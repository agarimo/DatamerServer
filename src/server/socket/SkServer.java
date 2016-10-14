package server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import socket.ClientSocket;
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
    private ExecutorService clientExecutor;
    Socket socket;
    private final int puerto;
    private boolean run = false;

    public SkServer(int puerto) {
        clientExecutor = Executors.newFixedThreadPool(8);
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
        clientExecutor.shutdownNow();
        clientExecutor=null;
    }

    private boolean disconnect() {
        Request request = new Request(ServerRequest.DISCONECT);
        Response sr;
        ClientSocket cs = new ClientSocket();
        try {
            cs.conect();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SkServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        sr = cs.sendRequest(request);
        System.out.println(sr.getResponse());

        return sr.getResponse().equals(ServerResponse.DISCONECTED);
    }

    public boolean isRunning() {
        return this.run;
    }

    public ExecutorService getClientExecutor() {
        return clientExecutor;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("SkServer Thread");
        try {
            servidor = new ServerSocket(puerto);

            while (run) {
                socket = servidor.accept();
                SocketCon sc = new SocketCon(socket);
                clientExecutor.execute(sc);
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
