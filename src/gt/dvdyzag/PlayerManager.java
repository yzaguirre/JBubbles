package gt.dvdyzag;

import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.io.File;

/**
 * Encargado de guardar los datos
 * del jugador
 * @author David Yzaguirre Gonzalez
 * Carne 200819312
 * Seccion B
 **/
public class PlayerManager {
    private String Nombre;
    public int Escenario;
    public File archivo;
    public int FichasEliminadas;
    public int Punteo;
    public int Disparos;
    //Ponderaciones
    private static final int ValorNormal = 3;
    private static final int ValorAgregado = 4;

    /**
     * Guarda la cantidad de fichase eliminadas y
     * manda los datos a la clase que genera reportes
     * html
     * @param totalFichas Son todas las fichas eliminadas
     * @param totalDisparos Son los disparos hechos por el jugador
     **/
    public void generarReporte(int totalFichas, int totalDisparos){
        FichasEliminadas = totalFichas;
        Disparos = totalDisparos;
        Reports Reports = new Reports(this);
        JOptionPane.showMessageDialog(null, "Se actualizo el siguiente reporte: \n"+Reports.getHTMLPath(),"Reportes",JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Suma los puntos ganados por eliminar fichas del mismo color
     * y las fichas que quedaron flotando
     * @param eliminadas Cantidad de fichas eliminadas del mismo color
     * @param flotantes Cantidad de fichas eliminadas por flotar
     **/
    public void guardarPunteo(int eliminadas, int flotantes){
        Punteo += eliminadas*ValorNormal+flotantes*ValorAgregado;
    }

    public void guardarArchivo(File archivo) {
        this.archivo = archivo;
    }

    public void guardarEscenario(int stage) {
        Escenario = stage;
    }

    public boolean guardarNombre(String name) {
        if ( name.equals("") ){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "No esta su nombre","Problema de ingreso",JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            Nombre = name;
            return true;
        }
    }

    /**
     * Verefica si se ha elegido escenario
     **/
    public boolean EscenarioElegido(boolean eleccion) {
        if ( !eleccion ){
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "No se ha elegido archivo de entrada","Problema de ingreso",JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    public String conseguirNombre(){
        return Nombre;
    }

    public int conseguirEscenario(){
        return Escenario;
    }

    public File conseguirArchivo(){
        return archivo;
    }
}