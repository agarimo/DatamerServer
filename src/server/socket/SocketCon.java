package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    public SocketCon(Socket socket) {
        this.socket = socket;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SocketCon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void desconectar(){
        try{
            socket.close();
        }catch(IOException ex){
            Logger.getLogger(SocketCon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        
    }

}
