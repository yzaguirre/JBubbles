package gt.dvdyzag;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import acercade.AcercaDe;

public class JBubbles extends JFrame {

	private JPanel contentPane;
	private PlayerManager playerManager;
    private GameScreen gameScreen;
    private CannonStatus cannonStatus;
    private MatrixPreview matrixPreview;
    private JFileChooser jfcArchivo;
    private File nivel;
    //indica que el usuario acepto el archivo
    private boolean acceptedFile;
    private AcercaDe acercade;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// Queueing GUI work to be run using the EDT.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					JBubbles frame = new JBubbles();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JBubbles() {
		initComponents();
		acercade = new AcercaDe(this);
        //Prepara al selector de archivos
        jfcArchivo = new JFileChooser();
        FileNameExtensionFilter filtroTexto = new FileNameExtensionFilter("Archivo de texto (*.txt)", "txt");
        jfcArchivo.setFileFilter(filtroTexto);

        matrixPreview = new MatrixPreview(jfcArchivo);
        jfcArchivo.addPropertyChangeListener(matrixPreview);
        jfcArchivo.setAccessory(matrixPreview);
		/*setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);*/
	}
	private void JTBescenarioUnoActionPerformed(java.awt.event.ActionEvent evt) {
        matrixPreview.saveScene(1);
        jfcArchivo.setDialogTitle("Abrir archivo para Escenario "+1);

        abrirArchivos();
        
    }

    private void JTBescenarioDosActionPerformed(java.awt.event.ActionEvent evt) {
        matrixPreview.saveScene(2);
        jfcArchivo.setDialogTitle("Abrir archivo para Escenario "+2);
        abrirArchivos();
    }

    private void JTBescenarioTresActionPerformed(java.awt.event.ActionEvent evt) {
        matrixPreview.saveScene(3);
        jfcArchivo.setDialogTitle("Abrir archivo para Escenario "+3);
        abrirArchivos();
    }

    private void JBiniciarJuegoActionPerformed(java.awt.event.ActionEvent evt) {
        playerManager = new PlayerManager();

        if (playerManager.guardarNombre(JTFnombre.getText()) & playerManager.EscenarioElegido(acceptedFile) & matrixPreview.isValidFile()) {
            playerManager.guardarEscenario(matrixPreview.scene);
            playerManager.guardarArchivo(nivel);
            crearJuego();
        } else if (acceptedFile && !matrixPreview.isValidFile()){
            javax.swing.JOptionPane.showMessageDialog(null, "El archivo de entrada no es valido","Problema de ingreso",javax.swing.JOptionPane.ERROR_MESSAGE);
        }

    }
    private void JBlimpiarCamposActionPerformed(java.awt.event.ActionEvent evt) {
        borrarDatos();
    }

    private void JMTerminarJuegoActionPerformed(java.awt.event.ActionEvent evt) {
        gameScreen.salir();
        JMTerminarJuego.setEnabled(false);
    }

    private void JMreiniciarActionPerformed(java.awt.event.ActionEvent evt) {
        matrixPreview.updateMatrix(nivel);
        gameScreen.reiniciar();
    }

    private void JMacercadeActionPerformed(java.awt.event.ActionEvent evt) {
        acercade.setVisible(true);
    }

    /**
     * Remueve el Canvas del juego
     * y promueve un nuevo usaurio
     * Este metodo es activado unicamente
     * por el hilo de la clase GameScreen_200819312
     **/
    public void limpiarJuego(){
        this.remove(gameScreen);
        this.remove(cannonStatus);
        borrarDatos();
        JPdatosUsuario.setVisible(true);
        JMTerminarJuego.setEnabled(false);
        JMreiniciar.setEnabled(false);
    }

    /**
     * Borra los datos ingresados en el
     * panel de registro
     **/
    private void borrarDatos(){
        BGroupSceneSelect.clearSelection();
        acceptedFile = false;
        JTFnombre.setText(null);
    }

    private void abrirArchivos(){
        int abrir = jfcArchivo.showOpenDialog(this);
        if (abrir == JFileChooser.APPROVE_OPTION) {
            nivel = jfcArchivo.getSelectedFile();
            acceptedFile = true;
            //JLmatrixPreview.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(MatrixPreview.matrix.getSource())));
        } else {
            BGroupSceneSelect.clearSelection();
            acceptedFile = false;
        }
    }

    private void crearJuego(){
        Dimension dimension;
        switch (playerManager.conseguirEscenario()) {
            case 1:
            case 2:
                dimension = new Dimension(10 * Bubble.DimeSize,
                        20 * Bubble.DimeSize + Cannon.Y / 2);
                break;
            default:
                dimension = new Dimension(10 * Bubble.DimeSize,
                        20 * Bubble.DimeSize + Cannon.Y);
        }
        gameScreen = new GameScreen(dimension, playerManager);
        JPdatosUsuario.setVisible(false);
        setSize(this.getWidth(), dimension.height + 100);
        gameScreen.setLocation(300, 10);
        add(gameScreen);
        //*************************************************************************
        cannonStatus = new CannonStatus(Cannon.getCurrentLoadedBubble(),
                Cannon.getStandByBubble());
        cannonStatus.setLocation(60, this.getHeight()/2-100);
        add(cannonStatus);
        gameScreen.addCannonStatus(cannonStatus);
        gameScreen.addGUI(this);
        JMTerminarJuego.setEnabled(true);
        JMreiniciar.setEnabled(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BGroupSceneSelect = new javax.swing.ButtonGroup();
        JPdatosUsuario = new javax.swing.JPanel();
        JLjuegoNuevo = new javax.swing.JLabel();
        JLnombre = new javax.swing.JLabel();
        JLescenario = new javax.swing.JLabel();
        JTBescenarioUno = new javax.swing.JToggleButton();
        JTBescenarioDos = new javax.swing.JToggleButton();
        JTBescenarioTres = new javax.swing.JToggleButton();
        JTFnombre = new javax.swing.JTextField();
        JBiniciarJuego = new javax.swing.JButton();
        JBlimpiarCampos = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        JMprincipal = new javax.swing.JMenu();
        JMTerminarJuego = new javax.swing.JMenuItem();
        JMreiniciar = new javax.swing.JMenuItem();
        JMayuda = new javax.swing.JMenu();
        JMacercade = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JBubbles");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/JBubbles.png")).getImage());

        JPdatosUsuario.setBackground(new java.awt.Color(185, 28, 224));

        JLjuegoNuevo.setFont(new java.awt.Font("Dialog", 1, 18));
        JLjuegoNuevo.setForeground(new java.awt.Color(122, 222, 36));
        JLjuegoNuevo.setText("Juego Nuevo");

        JLnombre.setFont(new java.awt.Font("Dialog", 1, 14));
        JLnombre.setForeground(new java.awt.Color(20, 206, 226));
        JLnombre.setText("Nombre");

        JLescenario.setFont(new java.awt.Font("Dialog", 1, 14));
        JLescenario.setForeground(new java.awt.Color(20, 206, 226));
        JLescenario.setText("Elija Escenario");

        BGroupSceneSelect.add(JTBescenarioUno);
        JTBescenarioUno.setText("1");
        JTBescenarioUno.setToolTipText("Escenario Uno");
        JTBescenarioUno.setFocusable(false);
        JTBescenarioUno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTBescenarioUnoActionPerformed(evt);
            }
        });

        BGroupSceneSelect.add(JTBescenarioDos);
        JTBescenarioDos.setText("2");
        JTBescenarioDos.setToolTipText("Escenario Dos");
        JTBescenarioDos.setFocusable(false);
        JTBescenarioDos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTBescenarioDosActionPerformed(evt);
            }
        });

        BGroupSceneSelect.add(JTBescenarioTres);
        JTBescenarioTres.setText("3");
        JTBescenarioTres.setToolTipText("Escenario Tres");
        JTBescenarioTres.setFocusable(false);
        JTBescenarioTres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JTBescenarioTresActionPerformed(evt);
            }
        });

        JBiniciarJuego.setText("Iniciar Juego");
        JBiniciarJuego.setToolTipText("Empezar a jugar");
        JBiniciarJuego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBiniciarJuegoActionPerformed(evt);
            }
        });

        JBlimpiarCampos.setText("Limpiar");
        JBlimpiarCampos.setToolTipText("Limpiar Campos");
        JBlimpiarCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBlimpiarCamposActionPerformed(evt);
            }
        });

		javax.swing.GroupLayout JPdatosUsuarioLayout = new javax.swing.GroupLayout(JPdatosUsuario);
        JPdatosUsuario.setLayout(JPdatosUsuarioLayout);
        JPdatosUsuarioLayout.setHorizontalGroup(
            JPdatosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdatosUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPdatosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPdatosUsuarioLayout.createSequentialGroup()
                        .addComponent(JBiniciarJuego, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addGap(82, 82, 82)
                        .addComponent(JBlimpiarCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JPdatosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(JLjuegoNuevo)
                        .addGroup(JPdatosUsuarioLayout.createSequentialGroup()
                            .addComponent(JLnombre)
                            .addGap(18, 18, 18)
                            .addComponent(JTFnombre))
                        .addGroup(JPdatosUsuarioLayout.createSequentialGroup()
                            .addComponent(JLescenario)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(JTBescenarioUno, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(JTBescenarioDos, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(JTBescenarioTres, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(413, 413, 413))
        );
        JPdatosUsuarioLayout.setVerticalGroup(
            JPdatosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPdatosUsuarioLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(JLjuegoNuevo)
                .addGap(39, 39, 39)
                .addGroup(JPdatosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLnombre)
                    .addComponent(JTFnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(JPdatosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLescenario)
                    .addComponent(JTBescenarioUno)
                    .addComponent(JTBescenarioDos)
                    .addComponent(JTBescenarioTres))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addGroup(JPdatosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JBlimpiarCampos, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(JBiniciarJuego, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(246, 246, 246))
        );

        JMprincipal.setMnemonic(java.awt.event.KeyEvent.VK_A);
        JMprincipal.setText("Archivo");

        JMTerminarJuego.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        JMTerminarJuego.setText("Juego Nuevo");
        JMTerminarJuego.setEnabled(false);
        JMTerminarJuego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMTerminarJuegoActionPerformed(evt);
            }
        });
        JMprincipal.add(JMTerminarJuego);

        JMreiniciar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        JMreiniciar.setText("Reiniciar Escenario");
        JMreiniciar.setEnabled(false);
        JMreiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMreiniciarActionPerformed(evt);
            }
        });
        JMprincipal.add(JMreiniciar);

        jMenuBar1.add(JMprincipal);

        JMayuda.setMnemonic(java.awt.event.KeyEvent.VK_U);
        JMayuda.setLabel("Ayuda");

        JMacercade.setLabel("Acerca de...");
        JMacercade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JMacercadeActionPerformed(evt);
            }
        });
        JMayuda.add(JMacercade);

        jMenuBar1.add(JMayuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPdatosUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPdatosUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
	}
    // Variables declaration
    private javax.swing.ButtonGroup BGroupSceneSelect;
    private javax.swing.JButton JBiniciarJuego;
    private javax.swing.JButton JBlimpiarCampos;
    private javax.swing.JLabel JLescenario;
    private javax.swing.JLabel JLjuegoNuevo;
    private javax.swing.JLabel JLnombre;
    private javax.swing.JMenuItem JMTerminarJuego;
    private javax.swing.JMenuItem JMacercade;
    private javax.swing.JMenu JMayuda;
    private javax.swing.JMenu JMprincipal;
    private javax.swing.JMenuItem JMreiniciar;
    private javax.swing.JPanel JPdatosUsuario;
    private javax.swing.JToggleButton JTBescenarioDos;
    private javax.swing.JToggleButton JTBescenarioTres;
    private javax.swing.JToggleButton JTBescenarioUno;
    private javax.swing.JTextField JTFnombre;
    private javax.swing.JMenuBar jMenuBar1;
    // End of variables declaration
}
