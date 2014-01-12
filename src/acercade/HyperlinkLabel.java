/*
 * HyperlinkLabel.java
 * Credito para el blog
 * http://jajatips.blogspot.com/2009/04/create-hyperlink-with-desktop.html
 * Creado el 25 de marzo de 2010, 06:06 PM
 */
package acercade;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;

/**
 * Hecho para abrir una pagina web en el navegador
 * predeterminado independiente del Sistema Operativo
 * @author David Yzaguirre Gonzalez
 */
public class HyperlinkLabel extends HyperlinkView {

    private String url;

    /**
     * Crea la etiqueta hipervinculo con el nombre a
     * a desplegar y la direccion que debe abrir
     * el navegador predeterminado
     * @param nombre Es el nombre a mostrar en el label
     * @param url Es la direccion que abre el navegador
     **/
    public HyperlinkLabel(String nombre, String url) {
        super(nombre, url);
        this.url = url;
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                abrirEnlace();
            }
        });
    }
    
    /**
     * Realiza la operacion de abrir el enlace suministrado
     * en el constructor
     **/
    public void abrirEnlace() {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
