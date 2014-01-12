package gt.dvdyzag;

import java.io.*;
import java.net.URL;

/**
 * Realiza todas las operacioens de archivos
 * para generar reportes en base a resultados
 * anteriores.
 * Hace un reporte para cada escenario (1 2 y 3)
 * Lee los resultados anteriores, compara la posicion respecto
 * al jugadora actual, genera el reporte en HTML y vuelve
 * a guardar el registro de los jugadores
 * @author David Yzaguirre Gonzalez
 * Carne 200819312
 * Seccion B
 */
public class Reports {
    //Contiene todos los datos necesarios para generar un reporte

    private PlayerManager playerManager;
    private BufferedReader lectorRegistro;
    private BufferedWriter bwHTML;
    private File fileHTML;
    //Numero del escenario
    private char num;
    //Parte HTML
    private String top;
    private String bottom;

    /**
     * Genera reportes en base a un jugador
     * @param playermanager Recibe los datos necesarios para generar reportes
     **/
    public Reports(PlayerManager pm) {
        playerManager = pm;
        num = String.valueOf(playerManager.Escenario).charAt(0);
        //URL url = getClass().getClassLoader().getResource("reportes/ReporteEscenario" + num + ".html");
        //fileHTML = new File( url.getPath() );

        top = "<html> \n  <head> \n <title> \n Reportes Escenario " + num +
                " \n </title> \n </head> \n <body> \n <h1> \n Listado de jugadores \n </h1>" +
                " \n <table border = " + "1" + "> \n <caption><B>Escenario "+num+"</B></caption> \n <tr>" +
                " \n <th> \n  #  \n </th>" +
                " \n <th> \n Nombre \n </th>" +
                " \n <th> \n Total de Disparos \n </th>" +
                " \n <th> \n Puntuaci&oacute;n \n </th> \n </tr>";

        bottom = " \n </table> \n </body> \n </html>";

        guardarRegistro(actualizarRegistro());
        escribirHTML();
    }

    /**
     * Lee los datos de jugadores anteriores
     **/
    private StringBuffer actualizarRegistro() {
        //Es el punto que sera comparado
        int punteo = playerManager.Punteo;
        StringBuffer CadenaRegistro = new StringBuffer();
        String linea;
        //Preparando para leer el registro
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(new File(getLocation("Datos/escenario" + num + ".txt")));
            br = new BufferedReader(fr);

            //dato = Integer.parseInt( br.readLine() );
            if ((linea = br.readLine()) == null) {//Si no existe registro
                CadenaRegistro.append(punteo + "\n" +
                        playerManager.conseguirNombre() + "\n" +
                        playerManager.Disparos + "\n");
                //guardarRegistro(CadenaRegistro);

            } else {
                boolean encontrado = false;//Si es true, lo aniade de ultimo
                while (linea != null) {//Recorre los datos guardados

                    if (!encontrado && punteo >= Integer.parseInt(linea)) {//Si encuentra un punteo menor
                        CadenaRegistro.append(punteo + "\n" +
                                playerManager.conseguirNombre() + "\n" +
                                playerManager.Disparos + "\n");
                        encontrado = true;
                    }
                    CadenaRegistro.append(linea + "\n" +
                            br.readLine() + "\n" +
                            br.readLine() + "\n");
                    linea = br.readLine();
                }//Cierra ciclo while (linea != null)

                if (!encontrado) {
                    CadenaRegistro.append(punteo + "\n" +
                            playerManager.conseguirNombre() + "\n" +
                            playerManager.Disparos + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return CadenaRegistro;
    }

    /**
     * Guarda un nuevo registro
     * en el archivo de texto
     **/
    private void guardarRegistro(StringBuffer stringBuffer) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        
        try {
            fw = new FileWriter(new File(getLocation("Datos/escenario" + num + ".txt")));
            bw = new BufferedWriter(fw);
            bw.write(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void escribirHTML() {
        //Prepara el html
        
        fileHTML = new File(getLocation("reportes/ReporteEscenario" + num + ".html"));
        FileWriter htmlWriter = null;

        //Prepara la lectura de los archivos de registro
        FileReader fr = null;
        

        try {
            fr = new FileReader(new File(getLocation("Datos/escenario" + num + ".txt")));
            lectorRegistro = new BufferedReader(fr);

            htmlWriter = new FileWriter(fileHTML);
            bwHTML = new BufferedWriter(htmlWriter);
            bwHTML.write(top);

            String temp = "";
            int i = 0;
            while ((temp = lectorRegistro.readLine()) != null) {
                bwHTML.write(" \n <tr> \n");
                bwHTML.write(" <td>" + ++i + "</td>");
                bwHTML.write("<td>" + lectorRegistro.readLine() + "</td>");
                bwHTML.write("<td>" + lectorRegistro.readLine() + "</td>");
                bwHTML.write("<td>" + temp + "</td>");
                bwHTML.write(" \n </tr>");
            }
            bwHTML.append(bottom);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (bwHTML != null) {
                    bwHTML.close();
                }

                if (lectorRegistro != null) {
                    lectorRegistro.close();
                }

                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Devuelve la direccion completa
     * del archivo
     **/
    private String getLocation(String dir) {
        URL url = getClass().getClassLoader().getResource(dir);
        dir = url.toString().replaceAll("%20", " ").replaceAll("file:", "");
        return dir;
    }

    /**
     * Devuelve la direccion del archivo
     * *.html generado
     **/
    public String getHTMLPath(){
        return fileHTML.getPath();
    }
}
