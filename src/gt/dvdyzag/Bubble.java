package gt.dvdyzag;

import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Esta es la clase que define los atributos y metodos
 * de la Ficha. hereda de BufferedImaged para tener
 * facilidad de dibujo.
 * Entre los sinonimos que escogi para designa una Ficha
 * estan: burbuja, bubble y dime
 * @author David Yzaguirre Gonzalez
 * @version 1.70
 * Carne 200819312
 * Seccion B
 **/
public class Bubble extends BufferedImage {
    //Constantes que son comparadas con un numero generado al azar
    public final static int AZUL = 0;
    public final static int VERDE = 1;
    public final static int ROJO = 2;
    public final static int AMARILLO = 3;

    /*Tamanio en pixeles de cada esferita**/
    public final static int DimeSize = 25;
    /*Indica el color de la burbuja*/
    private Color BubbleColor;
    /*Es el contador de todas las burbujas disponibles*/
    public static int Bubbles = 0;
    /*Indica el total de burbujas acumuladas durante la partida*/
    public static int TotalBubbles = 0;
    /*
     * Indica si se ha detectado mas de 2 fichas, mereciendo su eliminacion
     * La ficha disparada cuenta como la primera en ser eliminada
     * Por cada esferita eliminada, se tiene un bono de 3 pts
     */
    public static int DropCount = 0;
    /*
     * Indica la cantidad de Fichas eliminadas por flotar
     * Estas tienen un bono de 4 pts
     */
    public static int FloatCount = 0;
    /*Indica si (true) si la esfera esta flotando, de lo contrario false*/
    public boolean isFloating;
    /* 
     * "drop" viene del ingles que tiene un significado de "abatido"
     * Indica si la ficha se ha comparado con el color de otra ficha para ser eliminada
     * si es asi, vale true, de lo contrario es false
     */
    public boolean isDrop;

    /**
     * Genera una pelotita al azar para el canion
     * @param colorCode Para generar una pelotita al azar
     **/
    public Bubble(int colorCode){
        super(DimeSize,DimeSize,BufferedImage.TRANSLUCENT);

        Graphics2D g2d = this.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//antialiasing
        switch(colorCode){
            case AZUL:
                BubbleColor = grabColor('A');
                break;
            case VERDE:
                BubbleColor = grabColor('V');
                break;
            case ROJO:
                BubbleColor = grabColor('R');
                break;
            case AMARILLO:
                BubbleColor = grabColor('Y');
                break;
            default:
                BubbleColor = grabColor('0');
        }
        if (BubbleColor!=null){
            g2d.setColor(BubbleColor);
            g2d.fillOval(0,0,DimeSize,DimeSize);
        }
        g2d.dispose();
    }

    /**
     *	Genera pelotitas leido desde el archivo de texto
     *  @param DimeColor Es la letra leida desde el archivo de texto
     **/
    public Bubble(char DimeColor){
        super(DimeSize,DimeSize,BufferedImage.TRANSLUCENT);

        Graphics2D g2d = this.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//antialiasing
        BubbleColor = grabColor(DimeColor);

        if (BubbleColor!=null){
            g2d.setColor(BubbleColor);
            g2d.fillOval(0,0,DimeSize,DimeSize);
        }
        g2d.dispose();
    }

    /*/**
     * Genera una ficha creada a partir de la ficha
     * del parametro
     *	@param DimeColor es el color de la otra pelotita
     **/
    Bubble(Color DimeColor) {
        super(DimeSize, DimeSize, BufferedImage.TRANSLUCENT);

        Graphics2D g2d = this.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//antialiasing
        BubbleColor = DimeColor;

        g2d.setColor(BubbleColor);
        g2d.fillOval(0, 0, DimeSize, DimeSize);
        g2d.dispose();
    }

    /**
     * Devuelve el color segun el char
     * enviado de parametro
     * @param color Es la letra que representa un color
     **/
    private Color grabColor(char color) {
        switch (color){
            case 'A': return Color.BLUE;
            case 'V': return Color.GREEN;
            case 'R': return Color.RED;
            case 'Y': return Color.YELLOW;
            //case 'G': return Color.GRAY;
            default: return null;
        }
    }

    /**
     * Calcula las Fichas restantes
     **/
    public static void remainingBubbles(){
        Bubble.Bubbles -= DropCount+FloatCount;
    }

    public static void resetBubbles(){
        Bubbles = 0;
        TotalBubbles = 0;
    }

    /**
     * Devuelve la cantidad de Fichas
     * disponibles
     **/
    public static int getBubbleCount() {
        return Bubbles;
    }

    /**
     * Devuelve el color de la ficha
     **/
    public Color getBubbleColor(){
        return BubbleColor;
    }

    /**
     * Agrega 1 a la cuenta del
     * total Fichas disponibles
     **/
    public static void addBubble(){
        Bubbles+=1;
        TotalBubbles+=1;
    }

    /**
     * Devuelve una cadena
     * que detalla el color de
     * la ficha
     **/
    @Override
    public String toString(){
        if (this.BubbleColor==Color.RED)
            return "Rojo";
        if (this.BubbleColor==Color.BLUE)
            return "Azul";
        if (this.BubbleColor==Color.YELLOW)
            return "Amarillo";
        else
            return "Verde";
    }
}