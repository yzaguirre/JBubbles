package gt.dvdyzag;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;

/**
 * Este es el tablero del juego, aqui
 * se visualiza todo lo que el usuario
 * interactua con el juego
 * @autor David Yzaguirre Gonzalez
 * Carne 200819312
 * Seccion B
 **/
public class GameScreen extends Canvas implements MouseMotionListener,
        MouseListener,
        Runnable {
    //Dimensiones del lienzo

    public static Dimension DLienzo;
    //Ubicacion X del cannion
    private int MouseX;
    //Coordenas (x,y) de la Ficha disparada
    private int YFicha;
    private int XFicha;
    //Indica la columna en que debe caer la Ficha disparada
    private int XColumn;
    //Indica hasta donde debe dibujar la Ficha disparada
    private int Limit;
    //indica el escenario escogido
    private int escena;
    //si dispara el cannion arriba (true) o abajo (false)
    private boolean Sense;
    //Instancias para comunicar entre clases
    private CannonStatus cannonStatus;
    private Cannon cannon;
    private PlayerManager playerManager;
    private DimeManager dimeManager;
    private JBubbles GUI;
    //Ficha dibujada que recorre la trayectoria del disparo
    public Bubble Ficha;
    //Inicializa este hilo
    private Thread hiloFicha;
    //Indica que debe animar el disparo dado por el cannion
    private boolean banderaDisparo;
    //Indica si el hilo debe continuar ejecutandose
    private boolean banderaHilo;

    //@Override
    public void run() {
        while (banderaHilo) {
            /**
             * Si es false banderaDisparo, quiere decir
             * que el canon ha sido disparado
             **/
            while (!banderaDisparo) {
                try {
                    int i = YFicha * ((Sense) ? 1 : -1);
                    while (i > Limit) {//-5 para "rebotar", +5 para "mas veloz"
                        Thread.sleep(1000 / 150);//el denominador indica los cuadros por segundo
                        YFicha += ((Sense) ? -5 : 5);
                        i -= 5;
                        repaint();
                    }
                    dimeManager.addDime(Ficha, XColumn, Sense);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                banderaDisparo = true;
            }//Termina ciclo While(!banderaDisparo)
        }//Termina ciclo While(banderaHilo)
        GUI.limpiarJuego();
        System.out.println("Hilo Disparo terminado con exito");
    }

    /**
     * Realiza todo lo necesario para iniciar el juego
     * Inicializa la instancia del cannion, la matriz de fichas
     * e inicia el hilo encargado de la animacion de la ficha
     * disparado por el cannion
     **/
    GameScreen(Dimension dim, PlayerManager pm) {
        super();
        hiloFicha = new Thread(this);
        playerManager = pm;
        escena = playerManager.conseguirEscenario();
        DLienzo = dim;
        setSize(DLienzo);
        try {
            cannon = new Cannon(escena);
            dimeManager = new DimeManager(playerManager, DLienzo, this);
            banderaDisparo = true;
            banderaHilo = true;
            hiloFicha.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Aniada la instancia de la clase que controla
     * las Fichas que seran disparadas por el cannion
     **/
    public void addCannonStatus(CannonStatus CS) {
        cannonStatus = CS;
    }

    /**
     * Aniada la instancia de la interfaz que sirva para
     * controlar los cambios de usuario
     **/
    public void addGUI(JBubbles gui) {
        GUI = gui;
    }

    /**
     * Dibuja los objetos del juego, que incluye
     * el cannion, la matriz de fichas, y la ficha
     * disparada
     **/
    private void OffScreen(Graphics2D g2d) {
        //Le agrega fondo al lienzo
        //g2d.setColor(java.awt.Color.CYAN);
        //g2d.fillRect(0,0,DLienzo.width,DLienzo.height);

        //Dibuja el cannion
        g2d.drawImage(cannon, MouseX - 65 / 2, cannon.vertical, this);
        //Dibuja la Ficha disparada
        g2d.drawImage(Ficha, XFicha, YFicha, this);
        //Dibuja la matriz
        g2d.drawImage(dimeManager.getBuffer(), 0, 0, this);
    }

    /**
     * Crea un BufferedImage para dibujar
     * en memoria
     **/
    @Override
    public void update(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        BufferedImage BIAnimacion = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dOffScreen = BIAnimacion.createGraphics();

        OffScreen(g2dOffScreen);

        g2d.drawImage(BIAnimacion, 0, 0, this);
    }

    /**
     * Llama al metodo update
     **/
    @Override
    public void paint(Graphics g) {
        update(g);
    }

    public void detenerHilo() {
        banderaHilo = false;
        banderaDisparo = false;
    }

    /**
     *
     **/
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    /**
     *
     **/
    public void mouseMoved(MouseEvent e) {
        MouseX = e.getX();
        //MouseY = e.getY();
        repaint();
    }

    /**
     *
     **/
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Termina abruptamente el juego, sin generar
     * reportes. Primero detiene los hilos, al
     * detener este hilo, se elimina el objeto
     * Canvas de la interfaz de usuario
     **/
    public void salir() {
        dimeManager.detenerHilo();
        this.detenerHilo();
    }

    /**
     * Llama a los metodos del manejador de la matriz
     * para que empieze de nuevo, y cambia los valores
     * que maneja la clase Dime_200819312 y PlayerManager_200819312
     **/
    public void reiniciar() {
        playerManager.Punteo = 0;
        cannonStatus.Fired = 0;
        dimeManager.restart();
        dimeManager.redibujar();
        Ficha.Bubbles = MatrixPreview.ballCount;
        Ficha.TotalBubbles = MatrixPreview.ballCount;
    }

    /**
     *
     **/
    public void mousePressed(MouseEvent e) {
        if (banderaDisparo) {
            XFicha = e.getX() - Ficha.DimeSize / 2;
            //Ciclo consigue en que columna se dispara la Ficha
            for (int i = 1; i <= dimeManager.ColMax; i++) {
                if (e.getX() < i * Ficha.DimeSize) {
                    XColumn = i - 1;
                    if (e.getY() < cannon.vertical + ((escena == 2) ? 0 : Cannon.Y / 2)) {
                        Sense = true; //Hacia arriba
                        YFicha = cannon.vertical; //varia segun el escenario
                        Limit = (dimeManager.superficie[0][XColumn] +
                                dimeManager.ConstPared) * Ficha.DimeSize;
                    } else {
                        Sense = false;
                        YFicha = ((escena == 2) ? Cannon.Y / 2 - Ficha.DimeSize : 9 * Ficha.DimeSize + Cannon.Y);
                        //La suma de Limit fue multiplicado por -1 para concordar con el hilo
                        Limit = -YFicha - 20 * Ficha.DimeSize +
                                (dimeManager.superficie[1][9 - XColumn] +
                                dimeManager.ConstPared) * Ficha.DimeSize;
                    }
                    break;
                }
            }
            Ficha = cannon.getNextBubble();
            banderaDisparo = false;//Esto activa la parte del hilo
            //Actualiza las fichas listas para disparar por el cannion
            cannonStatus.updateCannon(cannon.getCurrentLoadedBubble(),
                    cannon.getStandByBubble());
        }
    }

    /**
     *
     **/
    public void mouseReleased(MouseEvent e) {
    }

    /**
     *
     **/
    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    /**
     *
     **/
    public void mouseExited(MouseEvent e) {
    }
}
