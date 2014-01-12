/*
 * PanelLogo.java
 *
 * Creada el 25 de marzo de 2010, 11:59 PM
 */
package acercade;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import javax.imageio.*;

/**
 * Dibuja el logo de la aplicacion
 * @author David Yzaguirre Gonzalez
 */
public class PanelLogo extends JPanel {

    BufferedImage bi;
    int x, y;

    public PanelLogo(String dir, int sqrSize) {
        super();
        this.setSize(sqrSize, sqrSize);
        URL url = null;
        try {
            url = getClass().getClassLoader().getResource(dir);
            bi = ImageIO.read(url);
            x = bi.getWidth();
            y = bi.getHeight();
            int lado = Math.max(x, y);
            double escala = sqrSize / (double) lado;
            x = (int) (escala * (double) x);
            y = (int) (escala * (double) y);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se pudo cargar la imagen");
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bi != null) {
            g.drawImage(bi, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }
}
