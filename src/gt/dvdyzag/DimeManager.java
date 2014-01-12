package gt.dvdyzag;

import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Manipular la matriz
 * de fichas
 * @author David Yzaguirre Gonzalez
 * Carne 200819312
 * Seccion B
 **/
public class DimeManager extends Thread {
    /**
     * es la matriz [y][x] donde y son filas e x son columnas
     **/
    private Bubble[][] Dime;
    /**
     * Almacenan las coordenadas de la segunda ficha detectada
     * del mismo color que la disparada
     **/
    private int Row;
    private int Col;
    /**
     * Indica los espacio vacantes
     * fila uno es la superior, fila dos es la inferior
     **/
    public int superficie[][];
    /*
     * Toma los valores 0-10 para escenario 3; 0-20 para escenarios 1 y 2
     * Su valor indica la posicion de fila vacia en la matriz despues
     * de la ultima ficha respecto al extremo opuesto del cannion
     */
    private int max;
    /*Cambia la Fuenta para mostrar el punteo actual*/
    private Font scoreFont;
    /*instancias para llevar un control de las demas clases*/
    private PlayerManager playerManager;
    private GameScreen gameScreen;
    /*Es en donde se dibuja la matriz*/
    private BufferedImage matrixBuffer;
    private Graphics2D g2d;
    /*Guarda dimensiones del lienzo*/
    private Dimension dim;
    /*Guarda una copia de la escena elegida*/
    private int escena;
    /*Controla la ejecucion del hilo, en bajar la matriz*/
    private boolean banderaHilo;
    /*
     * Consigue la posicion de la ficha del otro extremo del cannion
     * La que esta pegada a la pared
     */
    public int ConstPared;
    /*
     * Cambia la posicion de la matriz en el lienzo
     * llamado fps por "frames per second"
     */
    public int fps;
    /*Dimensiones de los indices de la matriz en j columnas*/
    public final int ColMax = 10;
    /*Dimensiones de los indices de la matriz en i filas*/
    public final int RowMax = 20;
    /**Es true cuando se termina el juego, de lo contrario es false**/
    private boolean gameOver;

    /**
     * Encargado de bajar a la matriz de fichas
     *
     **/
    @Override
    public void run() {
        while (banderaHilo) {
            try {
                sleep(5000);
                ConstPared++;
                int i = 0;
                /**
                 * Al inicio "fps" y "i" se sumaba
                 * 1 a la vez, pero causaba un parpadeo
                 * de animacion, la soluciones fue hacer
                 * menos redibujos con sumandole 5
                 **/
                while (i < Bubble.DimeSize) {
                    fps += 5;
                    sleep(1000 / 50);//el denominador indica los cuadros por segundo
                    redibujar();
                    i += 5;
                }
                if ((ConstPared + max) > RowMax / ((escena == 3) ? 2 : 1)) {
                    //entra aqui cuando las fichas llega al nivel del cannion
                    if (!gameOver) {
                        //Si no ha terminado el juego, mande a terminarlo
                        terminar(false);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }//termina bloque try-catch
        }//Termina while(true)
        System.out.println("Hilo Matriz terminado con exito");
    }

    DimeManager(PlayerManager pm, Dimension dim, GameScreen gs) {
        this.dim = new Dimension(dim);
        playerManager = pm;
        escena = playerManager.conseguirEscenario();
        gameScreen = gs;
        restart();
        scoreFont = new Font("Dialog", Font.BOLD, 15);
        redibujar();
        banderaHilo = true;
        this.start();//Inicia el hilo
    }

    /**
     * Agrega una ficha en la cola de determinada columna
     * Guarda en variables temporales la ubicacion dentro de la matriz
     * de la ficha recibida del cannion, para luego determinar si
     * se elimina por encontrar mas de 2 fichas del mismo color o no
     * Solo en los disparos se debe vereficar si el jugador elimino
     * todas las fichas
     * @param dime Es la ficha que se agrega a la matriz
     * @param column Es la columna en donde se agrega la ficha
     * @param sense Indica si es hacia la matriz superior o inferior
     **/
    public void addDime(Bubble dime, int column, boolean sense) {
        int ColDisp = column;
        int RowDisp = 0;
        gameScreen.Ficha = null;//La que fue disparada por el cannion
        if (sense) {
            Dime[superficie[0][column]][column] = dime;
            RowDisp = superficie[0][column];
            g2d.drawImage(dime,
                    Bubble.DimeSize * column,
                    Bubble.DimeSize * (superficie[0][column] + ConstPared),
                    gameScreen);

            superficie[0][column] += 1;
            eliminateColors(dime.getBubbleColor(), RowDisp, ColDisp, 0, 0);

            if (superficie[0][column] > max) {
                max = superficie[0][column];
            }
        } else {
            Dime[superficie[1][ 9 - column]][ 9 - column] = dime;
            RowDisp = superficie[1][ 9 - column];
            ColDisp = 9 - column;
            if (escena == 3) {//Si es la parte inferior
                g2d.drawImage(dime,
                        Bubble.DimeSize * column,
                        dim.height - Bubble.DimeSize * (superficie[1][9 - column] + ConstPared + 1 - 10),
                        gameScreen);
                superficie[1][9 - column] += 1;

                eliminateColors(dime.getBubbleColor(), RowDisp, ColDisp, 10, 1);
                if (superficie[1][9 - column] - 10 > max) {
                    max = superficie[1][9 - column] - 10;
                }
            } else {//Si es el escenario 2
                g2d.drawImage(dime,
                        Bubble.DimeSize * column,
                        dim.height - Bubble.DimeSize * (superficie[1][9 - column] + ConstPared + 1),
                        gameScreen);
                superficie[1][9 - column] += 1;

                //Enviar tambien sup = 1, lowlimitrow = 0 limitRow = superficie[1][9-column]
                eliminateColors(dime.getBubbleColor(), RowDisp, ColDisp, 0, 1);
                if (superficie[1][9 - column] > max) {
                    max = superficie[1][9 - column];
                }
            }

        }
        Bubble.addBubble();//aniada la cuenta total de fichas existentes
        /**
         * Revisa si se elimino Fichas. Observe cuando no encuentra Fichas
         * del mismo color, no se redibuja la matriz
         **/
        if (Bubble.DropCount >= 2) {
            //Elimina la ficha recibida por el cannion
            Dime[RowDisp][ColDisp] = null;
            //Elimina la ficha secundaria
            Dime[Row][Col] = null;

            /**
             * Actualiza la cantidad de fichas eliminadas,
             * sumandole 1, por no tomar en cuenta la ficha
             * disparada por el cannion
             **/
            Bubble.DropCount++;

            /*
             * Si se elimina fichas por ser del mismo color, busca si hay fichas flotando
             * Y actualiza los indicadores superficie[2][maxCol]
             */
            matrixCleanUp(sense);

            //Dar lo sumado
            playerManager.guardarPunteo(Bubble.DropCount, Bubble.FloatCount);
            Bubble.remainingBubbles();

            //Manda a redibujar la matriz
            redibujar();
            if (Bubble.getBubbleCount() == 0) {
                if (!gameOver) {
                    terminar(true);
                }
            }
        } else { //Si la ficha recibida no encontro suficientes fichas del mismo color
            Dime[RowDisp][ColDisp].isDrop = false;
            if (Dime[Row][Col] != null && Dime[Row][Col].isDrop) {
                Dime[Row][Col].isDrop = false;
            }
            gameScreen.repaint();//Actualiza el contenido en el Canvas principal
        }
        
        if ((ConstPared + max) > RowMax / ((escena == 3) ? 2 : 1)) {
            if (!gameOver) {
                terminar(false);
            }
        }
        //Reinicia los contadores de fichas eliminadas
        Bubble.DropCount = 0;
        Bubble.FloatCount = 0;
    }

    /**
     * Devuelve el BufferedImage con la matriz
     * dibujada segun el escenario
     *
     **/
    public BufferedImage getBuffer() {
        return matrixBuffer;
    }

    /**
     * Metodo recursivo, dado una ficha revisa los 4 espacios
     * a su alrededor; si tiene exito en encontrar una ficha
     * del mismo color, la marca de calidad de "revisada" por el "isDrop"
     * y la manda como parametro de nuevo al metodo.
     * @param actual Ficha analizada
     * @param r Fila de la Ficha "actual"
     * @param c Columna de la Ficha "actual"
     * @param min Limite superior minima, varia de 0 o 10
     * @param sup Referencia a la matriz superficie[sup][x], toma valor 0 o 1
     **/
    private void eliminateColors(Color actual, int r, int c, int min, int sup) {
        if (Dime[r][c] != null) {
            Dime[r][c].isDrop = true;
        }
        //Revisa la ficha de abajo de la actual
        if ((r + 1) < superficie[sup][c] && Dime[r + 1][c] != null && !Dime[r + 1][c].isDrop && Dime[r + 1][c].getBubbleColor() == actual) {
            if (Bubble.DropCount >= 1) {//es decir, si detecta 3 esferas del mismo color
                //Se elimina la ficha
                Dime[r + 1][c] = null;
            } else {//Si solo se ha detectado 2 Fichas del mismo color
                Row = r + 1;
                Col = c;
            }
            Bubble.DropCount++;
            eliminateColors(actual, r + 1, c, min, sup);//llamada recursiva
        }

        //Revisa la ficha de arriba de la actual
        if ((r - 1) >= min && Dime[r - 1][c] != null && !Dime[r - 1][c].isDrop && Dime[r - 1][c].getBubbleColor() == actual) {
            if (Bubble.DropCount >= 1) {
                Dime[r - 1][c] = null;
            } else {//Si solo se ha detectado 2 Fichas del mismo color
                Row = r - 1;
                Col = c;
            }
            Bubble.DropCount++;
            eliminateColors(actual, r - 1, c, min, sup);
        }

        //Revisa la ficha a la derecha de la actual
        if ((c + 1) < ColMax && Dime[r][c + 1] != null && !Dime[r][c + 1].isDrop && Dime[r][c + 1].getBubbleColor() == actual) {
            if (Bubble.DropCount >= 1) {
                Dime[r][c + 1] = null;
            } else {//Si solo se ha detectado 2 Fichas del mismo color
                Row = r;
                Col = c + 1;
            }
            Bubble.DropCount++;
            eliminateColors(actual, r, c + 1, min, sup);
        }

        //Revisa la ficha a la izquierda de la actual
        if ((c - 1) >= 0 && Dime[r][c - 1] != null && !Dime[r][c - 1].isDrop && Dime[r][c - 1].getBubbleColor() == actual) {
            if (Bubble.DropCount >= 1) {
                Dime[r][c - 1] = null;
            } else {//Si solo se ha detectado 2 Fichas del mismo color
                Row = r;
                Col = c - 1;
            }
            Bubble.DropCount++;
            eliminateColors(actual, r, c - 1, min, sup);
        }
    }

    /**
     * Para la parte de eliminar las fichas flotantes:
     * Crea un ciclo que recorre por columnas,
     * activando el metodo recursivo checkFloating
     * Para el escenario 1 y 2, la fila empieza de 0
     * Para el escenario 3 empieza de 0 para la matriz superior,
     * 10 para la matriz inferior
     * Para actualizar los indicadores superficie[2][10] hace lo parecido
     * a los metodos que inicializa la matriz al llegar a la ultima fila
     * @param Sense indica si el disparo es hacia arriba o abajo
     *              para el escenario 3, para el escenario 1 y 2
     *              es irrelevante. true si es para arriba, sino false
     **/
    private void matrixCleanUp(boolean Sense) {
        int sup = 0;
        int filaInicio = 0;

        switch (escena) {
            case 1:
                break;
            case 2:
                sup = 1;
                break;
            default://Si es escena 3
                if (!Sense) {
                    filaInicio = 10;
                    sup = 1;
                }
        }

        //Itera por columnas para eliminar las esferas flotando
        for (int c = 0; c < ColMax; c++) {
            checkFloating(filaInicio, c, superficie[sup][c]);
        }
        //rowStart es 0 para escenario 1 y 2; Es 0 o 10 para el escenario 3

        /**
         * Desafortunadamente, se vuelve a evaluar el tipo de escenario
         * Si es el escenario 3, volvera a tener que iterar por filas y
         * columnas tanto de la matriz superior e inferior para poder calcular
         * la variable "max"
         **/
        switch (escena) {
            case 1:
            case 2:
                max = 0;
                for (int c = 0; c < ColMax; c++) {//para esc 1 y 2
                    int f = filaInicio;
                    while (f < RowMax && Dime[f][c] != null && Dime[f][c].getBubbleColor() != null) {
                        f++;//f llega a un maximo de 20
                    }
                    if (f - filaInicio > max) {
                        max = f - filaInicio;
                    }
                    superficie[sup][c] = f;
                }
                break;
            default:
                max = 0;
                for (int i = 0; i < ColMax; i++) {
                    int j = 0;
                    while (j < RowMax / 2 && Dime[j][i] != null && Dime[j][i].getBubbleColor() != null) {
                        j++;
                    }
                    if (j > max) {
                        max = j;
                    }
                    superficie[0][i] = j;
                }
                //Inicia las iteraciones para la matriz inferior
                for (int i = 0; i < ColMax; i++) {
                    int j = 10;
                    while (j < RowMax && Dime[j][i] != null && Dime[j][i].getBubbleColor() != null) {
                        j++;
                    }
                    if (j - 10 > max) {
                        max = j - 10;
                    }
                    superficie[1][i] = j;
                }
        }
    }

    /**
     * Elimina las fichas que esten en calidad de flotando
     * para eso analiza su variable booleana  isFloating.
     * Este metodo es usado unicamente cuando se hayan eliminado fichas
     * Utiliza la recursividad, se indica la columna, y revisa toda
     * esa columna
     * @param row Ficha en la fila row
     * @param col Ficha en la columna col
     * @param limitRow Maximo sitio a buscar fichas Flotando
     * @param sense indica el sentido que se pasa al metodo establishBounds
     **/
    public void checkFloating(int rowStart, int col, int maxRow) {
        if (Dime[rowStart][col] != null && Dime[rowStart][col].getBubbleColor() != null) {
            if (Dime[rowStart][col].isFloating) {
                Bubble.FloatCount++;
                Dime[rowStart][col] = new Bubble('0');//borrar
            }
        }

        if (rowStart + 1 < maxRow) {
            if (Dime[rowStart][col] == null || Dime[rowStart][col].getBubbleColor() == null) {
                if (Dime[rowStart + 1][col] != null && Dime[rowStart + 1][col].getBubbleColor() != null) {
                    Dime[rowStart + 1][col].isFloating = true;
                }
            }

            checkFloating(rowStart + 1, col, maxRow);//llamada recursiva
        }
    }

    /**
     * Consigue redibujar la matriz sobre el BufferImage matrixBuffer
     * por medio del BufferedImage "temporal", aplicando el concepto
     * de dibujo en memoria.
     **/
    public void redibujar() {
        BufferedImage temporal = new BufferedImage(dim.width, dim.height, BufferedImage.TRANSLUCENT);
        g2d = temporal.createGraphics();
        switch (escena) {
            case 1:
                redibujarEscenario1();
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill3DRect(0, 0, dim.width, fps, true);
                break;
            case 2:
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill3DRect(0, dim.height - fps, dim.width, fps,true);
                redibujarEscenario2();
                break;
            default:
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fill3DRect(0, 0, dim.width, fps, true);
                g2d.fill3DRect(0, dim.height-fps, dim.width, fps, true);
                redibujarEscenario3();
        }
        g2d.setFont(scoreFont);

        g2d.setColor(Color.BLACK);
        g2d.drawString(String.valueOf(playerManager.Punteo), 10, 15);
        g2d.drawString(String.valueOf(Bubble.getBubbleCount()),125,15);
        g2d.drawString(String.valueOf(Bubble.TotalBubbles),200,15);

        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(playerManager.Punteo), 11, 16);
        g2d.drawString(String.valueOf(Bubble.getBubbleCount()),126,16);
        g2d.drawString(String.valueOf(Bubble.TotalBubbles),201,16);

        matrixBuffer = temporal;
        gameScreen.repaint();
    }

    /**
     * Dibuja segun las reglas del escenario uno
     **/
    private void redibujarEscenario1() {
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < ColMax; j++) {
                g2d.drawImage(Dime[i][j],
                        Bubble.DimeSize * j,
                        Bubble.DimeSize * i + fps,
                        null);
            }
        }
    }

    /**
     * Dibuja segun las reglas del escenario dos
     **/
    private void redibujarEscenario2() {
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < ColMax; j++) {
                g2d.drawImage(Dime[i][j],
                        dim.width - Bubble.DimeSize * (j + 1),
                        dim.height - Bubble.DimeSize * (i + 1) - fps,
                        null);
            }
        }
    }

    /**
     * Dibuja segun las reglas del escenario tres
     **/
    private void redibujarEscenario3() {
        for (int i = 0; i < max; i++)//antes era colmax/2
        {
            for (int j = 0; j < ColMax; j++) {
                g2d.drawImage(Dime[i][j],
                        Bubble.DimeSize * (j),
                        Bubble.DimeSize * (i) + fps,
                        null);
                g2d.drawImage(Dime[i + 10][j],
                        dim.width - Bubble.DimeSize * (j + 1),
                        dim.height - Bubble.DimeSize * (i + 1) - fps,
                        null);
            }
        }
    }

    /**
     * Detiene el hilo que baja la matriz de forma segura
     **/
    public void detenerHilo() {
        banderaHilo = false;
    }

    /**
     * Termina el juego dado dos condiciones:
     * -Una ficha toco el cannion
     * -Se elimino todas las fichas
     * @param estado true si se elimino todas las fichas, false si
     *          una Ficha toco el cannion
     **/
    private void terminar(boolean estado) {
        gameOver = true;
        this.detenerHilo();
        
        int Total = Bubble.TotalBubbles - Bubble.Bubbles;
        int Fire = CannonStatus.Fired;
        
        java.awt.Toolkit.getDefaultToolkit().beep();
        if (estado) {
            JOptionPane.showMessageDialog(gameScreen.getParent(),
                    "¡Eres un Ganador! " + playerManager.Punteo + " puntos." +
                    "\nTotal Disparos: " + Fire,
                    "Felicidades",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(gameScreen.getParent(),
                    "Llegaste a un total de " + playerManager.Punteo + " puntos." +
                    "\nTotal Disparos: " + Fire,
                    "Un poco mas de esfuerzo",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        playerManager.generarReporte(Total, Fire);
        gameScreen.detenerHilo();
    }

    /**
     * Reinicia la matriz de acuerdo a los datos proporcionados
     * por la clase MatrixPreview_200819312
     **/
    public void restart(){
        superficie = MatrixPreview.surface;
        max = MatrixPreview.maximumSurface;
        Dime = MatrixPreview.DimeMatrix;
        ConstPared = 0;
        fps = 0;
        Bubble.DropCount = 0;
        Bubble.FloatCount = 0;
    }
}
