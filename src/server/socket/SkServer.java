package server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agárimo
 */
public class SkServer implements Runnable {

    private ServerSocket servidor;
    
    private final int puerto;
    private boolean run;
    
    public SkServer(int puerto){
        this.puerto=puerto;
        this.run=true;
    }
    
    public void desconectar(){
        this.run=false;
    }
    
    public boolean isRunning(){
        return this.run;
    }

    @Override
    public void run() {
        
        try {
            servidor = new ServerSocket(puerto);
            
            while(run){
                Socket socket = servidor.accept();
                SocketCon sc = new SocketCon(socket);
                new Thread(sc).start();
            }
            
            servidor.close();
            
        } catch (IOException ex) {
            run=false;
            Logger.getLogger(SkServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
