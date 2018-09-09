package server.download;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Agárimo
 */
public class Boletin {

    private final LocalDate fecha;
    private String entidad;
    private String origen;

    private final String preLink = "https://www.boe.es";
    private final List<Publicacion> list;

    public Boletin(LocalDate fecha) {
        this.fecha = fecha;
        list = new ArrayList();
    }
    
    public List<Publicacion> getPublicaciones(){
        return this.list;
    }

    public void setEntidad(String entidad) {
        entidad = entidad.replace("<h5>", "");
        entidad = entidad.replace("</h5>", "");
        entidad = entidad.trim();

        this.entidad = entidad;
    }

    private void setOrigen(String origen) {
        origen = origen.replace("<h6>", "");
        origen = origen.replace("</h6>", "");
        this.origen = origen.trim();
    }

    public void setDatos(String datos) {
        StringBuilder buffer = new StringBuilder();
        boolean print = false;
        String linea;
        String[] split = datos.split(System.getProperty("line.separator"));

        for (String a : split) {

            if (a.contains("<h6>")) {
                setOrigen(a);
            }

            if (a.contains("<li class=\"notif\">")) {
                print = true;
            }

            if (print) {
                buffer.append(a);
                buffer.append(System.getProperty("line.separator"));
            }

            if (a.contains("</li>")) {
                print = false;
                linea = buffer.toString();
                splitPdf(linea);
                buffer = new StringBuilder();
            }
        }
    }

    private void splitPdf(String linea) {
        String descripcion = null;
        String link = null;
        String codigo = null;

        String[] split = linea.split(System.getProperty("line.separator"));

        for (String a : split) {

            if (a.contains("<p>")) {
                descripcion = splitDescripcion(a);
            }

            if (a.contains("<a href=\"")) {
                link = splitLink(a);
            }
        }
        codigo = splitCodigo(link);
        creaPublicacion(codigo, descripcion, link);
    }

    private String splitDescripcion(String descripcion) {
        descripcion = descripcion.replace("<p>", "");
        descripcion = descripcion.replace("</p>", "");
        return descripcion.trim();
    }

    private String splitLink(String link) {
        String[] split = link.split("title=");

        link = split[0];
        link = link.replace("<a href=\"", "");
        link = link.replace("\"", "");
        return link.trim();
    }

    private String splitCodigo(String codigo) {
        String[] split = codigo.split("id=");
        return split[1].trim();
    }

    private void creaPublicacion(String codigo, String descripcion, String link) {
        Publicacion aux = new Publicacion();
        aux.setFecha(fecha);
        aux.setCodigo(codigo);
        aux.setEntidad(entidad);
        aux.setOrigen(origen);
        aux.setDescripcion(descripcion);
        aux.setDatos("PENDING");
        aux.setLink(preLink + link);
        aux.setCve("PENDING");
        aux.setSelected(null);
        aux.setStatus(Status.PENDING);
        
        list.add(aux);
    }

}
