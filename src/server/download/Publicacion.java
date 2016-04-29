package server.download;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import server.Var;
import util.Varios;

/**
 *
 * @author Agárimo
 */
public class Publicacion {
    private int id;
    private LocalDate fecha;
    private String codigo;
    private String entidad;
    private String origen;
    private String descripcion;
    private String datos;
    private String link;
    private String cve;
    private Boolean selected;
    private Status status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return this.codigo +" "+this.origen+" "+this.descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.codigo);
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
        final Publicacion other = (Publicacion) obj;
        return Objects.equals(this.codigo, other.codigo);
    }
    
    public String SQLCrear() {
        return "INSERT into " + Var.dbName + ".publicacion (fecha,codigo,entidad,origen,descripcion,datos,link,cve,selected,status) values("
                + Varios.comillas(this.fecha.format(DateTimeFormatter.ISO_LOCAL_DATE)) + ","
                + Varios.comillas(this.codigo) + ","
                + Varios.comillas(this.entidad) + ","
                + Varios.comillas(this.origen) + ","
                + Varios.comillas(this.descripcion) + ","
                + Varios.comillas(this.datos) + ","
                + Varios.comillas(this.link) + ","
                + Varios.comillas(this.cve) + ","
                + this.selected + ","
                + Varios.comillas(this.status.toString())
                + ");";
    }

    public String SQLBuscar() {
        return "SELECT * FROM " + Var.dbName + ".publicacion WHERE codigo=" + Varios.comillas(this.codigo);
    }
}
