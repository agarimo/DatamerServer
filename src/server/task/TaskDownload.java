package server.task;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import tools.LoadFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
import server.Var;
import server.download.Boletin;
import server.download.Publicacion;
import socket.enty.ModeloTarea;
import socket.enty.ServerTask;
import tools.Util;

/**
 *
 * @author Agárimo
 */
public class TaskDownload extends Tarea implements Runnable {

    private LocalDate fecha;
    private final File pdf;
    private final File txt;
    private List<Publicacion> boe;

    public TaskDownload(ModeloTarea modeloTarea) {
        super(modeloTarea);

        if (modeloTarea.getParametros() != null) {
            this.fecha = LocalDate.parse(modeloTarea.getParametros());
            System.out.println(this.fecha.format(DateTimeFormatter.ISO_DATE));
        } else {
            this.fecha = null;
        }
        

        pdf = new File(Var.fileSystem, "dwl.pdf");
        txt = new File(Var.fileSystem, "dwl.txt");
    }

    @Override
    public void run() {
        init();
        initTarea();
        download();
        endTarea();
        clasificacion();
    }

    private void init() {
        if (this.fecha == null || tarea.getPropietario().equalsIgnoreCase("SCHEDULER")) {
            this.fecha = LocalDate.now();
        }

        Thread.currentThread().setName("TaskDownload Thread");
        setTitulo("DOWNLOAD");
        setMensaje("Iniciando");
        setPorcentaje(0, 0);
        tarea.setParametros(this.fecha.format(DateTimeFormatter.ISO_DATE));
    }

    private void download() {
        cleanDB();
        initBoe();
        duplicados();

        setMensaje("Descargando");
        boe.stream().forEach((aux) -> {
            status = val + " de " + boe.size();
            status = status.replace(".0", "");
            setPorcentaje(val, boe.size());

            if (descarga(aux.getLink())) {
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
        clean();
    }

    private void cleanDB() {
        setMensaje("Preparando DB");
        try {
            String query = "DELETE from boes.boe where DATEDIFF(curdate(),fecha)> 10";
            bd.ejecutar(query);
        } catch (SQLException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initBoe() {
        setMensaje("Init BOE");
        try {
            String query = "INSERT INTO boes.boe (fecha,link,isClas) VALUES (" + Util.comillas(fecha.format(DateTimeFormatter.ISO_DATE)) + "," + Util.comillas(generaLink()) + ",0);";
            bd.ejecutar(query);
        } catch (SQLException ex) {
//            System.out.println("Excepción en TaskDownload.creaBoe();" + ex.getMessage());
        }
        boe = splitUrl(getUrl(generaLink()));
    }

    private void clean() {
        setMensaje("Finalizando");
        pdf.delete();
        txt.delete();
    }

    private void clasificacion() {
        ModeloTarea mt = this.getModeloTarea();
        mt.setTipoTarea(ServerTask.BOE_CLASIFICACION);
        mt.setPropietario(super.tarea.getPropietario());
        mt.setParametros("-");
        TaskClasificacion task = new TaskClasificacion(mt);
        task.run();
    }

    //<editor-fold defaultstate="collapsed" desc="GET URL">
    private String generaLink() {
        setMensaje("Cargando BOE");
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
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
//            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TaskDownload.class.getName()).log(Level.SEVERE, null, ex);
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
            setMensaje("Downloading " + status);
            tools.Download.downloadFILE(link, pdf);
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

        if (pNum > 3) {
            pNum = 3;
        }

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
        LoadFile.writeFile(txt, LoadFile.readFile(txt));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INSERCIÓN EN DB">
    private void duplicados() {
        setMensaje("Comprobando duplicados");
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
