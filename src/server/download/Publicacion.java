package server.download;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import server.Var;
import tools.Util;

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

    public void setStatus(String status) {

        switch (status) {
            case "APP":
                this.status = Status.APP;
                break;
            case "DELETED":
                this.status = Status.DELETED;
                break;
            case "PENDING":
                this.status = Status.PENDING;
                break;
            case "SOURCE":
                this.status = Status.SOURCE;
                break;
            case "USER":
                this.status = Status.USER;
                break;
            default:
                this.status = Status.PENDING;
                break;
        }
    }

    @Override
    public String toString() {
        return this.codigo + " " + this.origen + " " + this.descripcion;
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
                + Util.comillas(this.fecha.format(DateTimeFormatter.ISO_LOCAL_DATE)) + ","
                + Util.comillas(this.codigo) + ","
                + Util.comillas(this.entidad) + ","
                + Util.comillas(this.origen) + ","
                + Util.comillas(this.descripcion) + ","
                + Util.comillas(this.datos) + ","
                + Util.comillas(this.link) + ","
                + Util.comillas(this.cve) + ","
                + this.selected + ","
                + Util.comillas(this.status.toString())
                + ");";
    }

    public String SQLBuscar() {
        return "SELECT * FROM " + Var.dbName + ".publicacion WHERE codigo=" + Util.comillas(this.codigo);
    }

    public String SQLEditarStatus() {
        return "UPDATE " + Var.dbName + ".publicacion SET "
                + "selected=" + this.selected + ","
                + "status=" + Util.comillas(this.status.toString()) + " "
                + "WHERE id=" + this.id;
    }
}
