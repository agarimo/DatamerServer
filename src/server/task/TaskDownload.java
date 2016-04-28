package server.task;

import files.LoadFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.concurrent.Task;
import server.download.Boletin;
import server.download.Publicacion;

/**
 *
 * @author Agárimo
 */
public class TaskDownload extends Task {

    private final LocalDate fecha;
    private final File pdf;
    private final File txt;
    private double val;
    private String status;

    List<Publicacion> boe;

    public TaskDownload() {
        this.fecha = LocalDate.now();
        pdf = new File(new File("data"), "dwl.pdf");
        txt = new File(new File("data"), "dwl.txt");
    }

    public TaskDownload(LocalDate fecha) {
        this.fecha = fecha;
        pdf = new File(new File("data"), "dwl.pdf");
        txt = new File(new File("data"), "dwl.txt");
    }

    @Override
    protected Object call() throws Exception {
        this.updateMessage("Cargando BOLETINES");
        val = 1;
        boe = splitUrl(getUrl(generaLink()));
        //Buscar posibles duplicados ya insertados en la DB.
      

        boe.stream().forEach((aux) -> {
            status = val + " de " + boe.size();
            this.updateProgress(val, boe.size());
            
            if (descarga(aux.getLink())) {
                this.updateMessage("Updating "+status);
                LoadFile lf = new LoadFile(txt);
                aux.setCve(getCve(lf.getLineas()));
                
                //crear Método en LoadFile para extraer el texto completo y limpio.
                //aux.setDatos(lf.getLineas());
            } else {
                aux.setCve("No disponible");
            }
            
            //INSERT EN DB;
        });

        clean();
        return null;
    }

    private void clean() {
        this.updateMessage("Finalizando proceso");
        pdf.delete();
        txt.delete();
    }

    //<editor-fold defaultstate="collapsed" desc="GET URL">
    private String generaLink() {
        return "http://boe.es/boe_n/dias/" + fecha.format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
    }

    private String getUrl(String url) {
        boolean print = false;
        String inputLine;
        StringBuilder buffer = new StringBuilder();

        try {
            URL enl = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(enl.openStream()));

            while ((inputLine = in.readLine()) != null) {

                if (inputLine.contains("<div class=\"SumarioNotif\">")) {
                    print = true;
                }

                if (print) {
                    buffer.append(inputLine);
                    buffer.append(System.getProperty("line.separator"));
                }

                if (inputLine.contains("<div class=\"espacio\">")) {
                    print = false;
                }
            }
        } catch (MalformedURLException ex) {
            System.out.println("WEB NO DISPONIBLE");
        } catch (IOException ex) {
            System.out.println("WEB NO DISPONIBLE");
        }

        return buffer.toString();
    }

    private List splitUrl(String datos) {
        List aux = new ArrayList();
        Boletin boletin = new Boletin(this.fecha);
        String entidad = "";
        boolean print = false;
        boolean printP = false;
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        String[] split;

        split = datos.split(System.getProperty("line.separator"));

        for (String a1 : split) {

            if (a1.contains("<h5>")) {
                printP = true;
            }

            if (printP) {
                sb2.append(a1);
                sb2.append(System.getProperty("line.separator"));
            }

            if (a1.contains("</h5>")) {
                printP = false;
                entidad = sb2.toString();
                sb2 = new StringBuilder();
            }

            if (a1.contains("<h6>")) {
                print = true;
            }

            if (print) {
                sb1.append(a1);
                sb1.append(System.getProperty("line.separator"));
            }

            if (a1.contains("</ul>")) {
                print = false;
                boletin.setDatos(sb1.toString());
                boletin.setEntidad(entidad);
                aux.addAll(boletin.getPublicaciones());
                boletin = new Boletin(this.fecha);
                sb1 = new StringBuilder();
            }
        }
        return aux;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DESCARGA">
    private boolean descarga(String link) {
        try {
            this.updateMessage("Downloading " + status);
            files.Download.downloadFILE(link, pdf);
            this.updateMessage("Parsing " + status);
            files.Pdf.convertPDF(pdf, txt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getCve(List lineas) {
        String aux = "";
        Iterator<String> it = lineas.iterator();

        while (it.hasNext()) {
            aux = it.next();

            if (aux.contains("cve: BOE-N-")) {
                aux = aux.replace("cve: ", "").trim();
                System.out.println(aux);
                break;
            }
        }
        return aux;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INSERCIÓN EN DB">
//</editor-fold>
}
