package gt.dvdyzag;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Muestra en pantalla el estado de las fichas
 * con el cannion, mostrando primero la Ficha
 * lista para disparar en la parte superior,
 * y en la parte inferior muestra la Ficha
 * que viene despues.
 * @author David Yzaguirre Gonzalez
 * Carne 200819312
 * Seccion B
 * @see java.awt.Canvas
 **/
public class CannonStatus extends Canvas {

    //Ficha "cargada"
    private Bubble Loaded;
    //Ficha "en espera"
    private Bubble StandBy;
    //Letra de los mensajes
    private Font font;
    //Cantidad de disparos totales
    public static int Fired;

    /**
     * Crea el canvas con las dimensiones
     * necesarias para visualizar las dos
     * fichas que entraran en el juego
     * @param dime1
     * @param dime2
     **/
    CannonStatus(Bubble dime1, Bubble dime2){
        super();
        Dimension panelSize = new Dimension(220 ,80);
        setSize(panelSize);
        font = new Font("Dialog", Font.BOLD, 15);
        Loaded = dime1;
        StandBy = dime2;
    }
    
    /**
     * Actualiza el panel con las Fichas
     * generadas por el cannion
     * @param dime1 
     * @param dime2
     **/
    public void updateCannon(Bubble dime1, Bubble dime2) {
        Fired++;
        Loaded = dime1;
        StandBy = dime2;
        repaint();
    }

    /**
     * Crea un BufferedImage y dibuja
     * las fichas actuales sobre el,
     * luego se dibuja sobre el canvas
     * @param g
     **/
    @Override
    public void update(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        BufferedImage BI = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dBI = BI.createGraphics();

        g2dBI.setColor(Color.DARK_GRAY);
        g2dBI.fill3DRect(0, 0, getWidth(), getHeight(), true);

        g2dBI.setFont(font);

        g2dBI.drawImage(Loaded, 10, 10, this);
        g2dBI.setColor(Loaded.getBubbleColor());
        g2dBI.drawString("Siguente es "+Loaded, 50, 25);

        g2dBI.drawImage(StandBy, 10, 45, this);
        g2dBI.setColor(StandBy.getBubbleColor());
        g2dBI.drawString("Luego viene "+StandBy, 50, 60);

        g2d.drawImage(BI, 0, 0, this);
    }

    /**
     * Llama al metodo update
     * @param g
     **/
    @Override
    public void paint(Graphics g){
        update(g);
    }
}
