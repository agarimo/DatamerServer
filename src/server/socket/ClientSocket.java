package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import socket.enty.Request;
import socket.enty.Response;
import socket.enty.ServerRequest;
import socket.enty.ServerResponse;

/**
 *
 * @author Agárimo
 */
public class ClientSocket {

    private String host;
    private int port;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientSocket() {
        this.host = "127.0.0.1";
        this.port = 10987;
    }

    public ClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean conect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Response aux;

            out.writeObject(new Request(ServerRequest.CONNECT));
            aux = (Response) in.readObject();

            return aux.getResponse().equals(ServerResponse.CONECTED);

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean disconect() {
        try {
            Response aux;

            out.writeObject(new Request(ServerRequest.DISCONECT));
            aux = (Response) in.readObject();

            if (aux.getResponse().equals(ServerResponse.DISCONECTED)) {
                in.close();
                out.close();
                socket.close();
                return true;
            } else {
                return false;
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public synchronized Response sendRequest(Request request) {
        try {
            Response aux;

            out.writeObject(request);
            aux = (Response) in.readObject();

            return aux;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
