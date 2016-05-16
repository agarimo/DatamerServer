package server.task;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import files.LoadFile;
import files.Util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import server.Var;
import server.download.Boletin;
import server.download.Publicacion;
import socket.enty.ModeloTarea;
import sql.Sql;

/**
 *
 * @author Agárimo
 */
public class TaskDownload extends Tarea {

    private final LocalDate fecha;
    private final File pdf;
    private final File txt;
    private double val;
    private String status;

    private List<Publicacion> boe;
    private Sql bd;

    public TaskDownload(ModeloTarea modeloTarea) {
        super(modeloTarea);
        this.fecha = LocalDate.now();
        pdf = new File(Var.fileSystem, "dwl.pdf");
        txt = new File(Var.fileSystem, "dwl.txt");

    }

    public TaskDownload(LocalDate fecha, ModeloTarea modeloTarea) {
        super(modeloTarea);
        this.fecha = fecha;
        pdf = new File(Var.fileSystem, "dwl.pdf");
        txt = new File(Var.fileSystem, "dwl.txt");

    }

    @Override
    protected Object call() {
        try {
            System.out.println("Iniciando Download");

            Platform.runLater(() -> {
                this.updateMessage("Cargando BOLETINES");
                this.tarea.setProgreso(this.getMessage());
            });

            val = 1;
            boe = splitUrl(getUrl(generaLink()));

            conectar();
            duplicados();

            boe.stream().forEach((aux) -> {
                status = val + " de " + boe.size();
                status = status.replace(".0", "");
                System.out.println(status);

                Platform.runLater(() -> {
                    this.updateProgress(val, boe.size());
                    this.tarea.setPorcentaje(Double.toString(this.getProgress()) + " %");
                });

                if (descarga(aux.getLink())) {

                    Platform.runLater(() -> {
                        this.updateMessage("Updating " + status);
                        this.tarea.setProgreso(this.getMessage());
                    });

                    LoadFile lf = new LoadFile(txt);
                    aux.setCve(getCve(lf.getLineas()));
                    aux.setDatos(lf.getFileData());
                } else {
                    aux.setCve("No disponible");
                    aux.setDatos("No disponible");
                }

                insert(aux);
                val++;
            });

            desconectar();
            clean();
            System.out.println("Finalizado Descarga");
        } catch (Exception ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void clean() {
        Platform.runLater(() -> {
            this.updateMessage("Finalizando proceso");
            this.tarea.setProgreso(this.getMessage());
        });

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
                boletin.setEntidad(entidad);
                boletin.setDatos(sb1.toString());
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
            Platform.runLater(() -> {
                this.updateMessage("Downloading " + status);
            });

            files.Download.downloadFILE(link, pdf);

            Platform.runLater(() -> {
                this.updateMessage("Parsing " + status);
            });

            convertPDF(pdf, txt);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
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
                break;
            }
        }
        return aux;
    }

    private void convertPDF(File origen, File destino) throws IOException {
        destino.createNewFile();

        FileWriter fw = new FileWriter(destino.getAbsolutePath());
        BufferedWriter bw = new BufferedWriter(fw);
        PdfReader pr = new PdfReader(origen.getAbsolutePath());
        int pNum = pr.getNumberOfPages();
        for (int page = 1; page <= pNum; page++) {
            String text = PdfTextExtractor.getTextFromPage(pr, page);
            bw.write(text);
            bw.newLine();
        }
        bw.flush();
        bw.close();
        fw.close();
        pr.close();

        convertPDFFixFile(destino);
    }

    private void convertPDFFixFile(File txt) {
        String datos = Util.leeArchivo(txt);
        Util.escribeArchivo(txt, datos);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INSERCIÓN EN DB">
    private boolean conectar() {
        System.out.println("Conectando");
        try {
            bd = new Sql(Var.con);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean desconectar() {
        try {
            bd.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void duplicados() {
        try {
            Publicacion aux;
            List clear = new ArrayList();
            List duplicados = bd.getStringList("SELECT codigo FROM " + Var.dbName + ".publicacion");
            Iterator<Publicacion> it = boe.iterator();

            while (it.hasNext()) {
                aux = it.next();

                if (!duplicados.contains(aux.getCodigo())) {
                    clear.add(aux);
                }
            }

            boe.clear();
            boe.addAll(clear);
        } catch (SQLException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insert(Publicacion aux) {
        try {
            bd.ejecutar(aux.SQLCrear());
        } catch (SQLException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//</editor-fold>
}
