package server.socket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Agárimo
 */
public class Request implements Serializable {
    
    String tipo;
    List parametros;
    
    public Request(){
        this.tipo="";
        parametros=new ArrayList();
    }
    
    public Request(String tipo, List parametros){
        this.tipo=tipo;
        this.parametros=parametros;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        hash = 31 * hash + Objects.hashCode(this.tipo);
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
        final Request other = (Request) obj;
        return Objects.equals(this.tipo, other.tipo);
    }

    @Override
    public String toString() {
        return "Request{" + "tipo=" + tipo + ", parametros=" + parametros + '}';
    }
}
