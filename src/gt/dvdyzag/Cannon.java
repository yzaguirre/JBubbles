package gt.dvdyzag;

import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Carga la imagen de un cannion
 * para ser insertado en el lienzo
 * @author David Yzaguirre Gonzalez
 * Carne 200819312
 * Seccion B
 **/
public class Cannon extends BufferedImage{
    //private final int Width = 150;
    //Guarda la posicion vertical respecto al canvas
    public int vertical;
    //Deben ser estaticos para que sean accesados sin instanciar la clase
    private static Bubble BurbujaUno;
    private static Bubble BurbujaDos;
    private static Random random;
    public static int Y = 106;
    public static int X = 65;
    //private final int Height = 175;

    /**
     * Dibuja el cannion desde la imagen *.png
     * sobre el BufferedImage
     * Establece la variable vertical que tendra
     * un valor constante durante todo el juego
     * segun la escena elegida
     * Para generar la Fichas al azar se usa la clase
     * java.util.Random
     **/
    public Cannon(int escenario) throws IOException {
        super(X, Y, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = this.createGraphics();
        //URLClassLoader url = new URLClassLoader("imagenes/Cannon.png");
        URL url = getClass().getClassLoader().getResource("imagenes/Cannon.png");
        Image imagen = ImageIO.read( url );
        imagen.flush();
        g2d.drawImage( imagen , 0, 0, null );
        g2d.dispose();
        switch (escenario){
        case 1:
            vertical = GameScreen.DLienzo.height-getHeight()/2;
            break;
        case 2:
            vertical = -getHeight()/2;
            break;
        default:
            vertical = GameScreen.DLienzo.height/2-getHeight()/2;
        }
        random = new Random();
        BurbujaUno = new Bubble( random.nextInt(4) );
        BurbujaDos = new Bubble( random.nextInt(4) );
    }

    /**
     * Devuelve la Ficha que es proxima a
     * ser disparada, BurbujaUno recibe a
     * BurbujaDos, y BurbujaDos es dado
     * una ficha al azar
     **/
    public Bubble getNextBubble(){
        Bubble aux = BurbujaUno;
        BurbujaUno = BurbujaDos;
        BurbujaDos = new Bubble( random.nextInt(4) );
        return aux;
    }

    /**
     * Devuelve la Ficha que actualmente
     * lista para ser disparada por
     * el cannion
     **/
    public static Bubble getCurrentLoadedBubble(){
        return BurbujaUno;
    }

    /**
     * Devuelve la Ficha que se disparara despues
     * de la Ficha actual cargada en el cannion
     **/
    public static Bubble getStandByBubble(){
        return BurbujaDos;
    }
}
