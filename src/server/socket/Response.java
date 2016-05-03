package server.socket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Agárimo
 */
public class Response implements Serializable {
    String status;
    List parametros;
    
    public Response(){
        this.status="";
        this.parametros = new ArrayList();
    }
    
    public Response(String status, List parametros){
        this.status=status;
        this.parametros= parametros;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getParametros() {
        return parametros;
    }

    public void setParametros(List parametros) {
        this.parametros = parametros;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Response other = (Response) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Response{" + "status=" + status + ", parametros=" + parametros + '}';
    }
}
