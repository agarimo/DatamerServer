package server.task;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Agárimo
 */
public class Tasker {
    
    private List running_task;
    
    
    public Tasker(){
        running_task= new ArrayList();
    }

    public List getRunning_task() {
        return running_task;
    }

    public void setRunning_task(List running_task) {
        this.running_task = running_task;
    }
    
    
}
