
package org.joseruiz.report;

import java.util.Map;
import java.io.InputStream;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.joseruiz.db.Conexion;

public class GenerarReporte {
        
    public static void mostrarReporte(String nombreReporte, String titulo, Map parametros){//Mostrará el reporte.
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);//Interpretamos y verificamos el archivo
        try {
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);//Aca esta el reporte completo
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametros, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false);//Es el objeto que permite que veamos el reporte
            visor.setTitle(titulo);
            visor.setVisible(true);//Hace visible el reporte
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}