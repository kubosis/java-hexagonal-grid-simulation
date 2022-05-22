package cz.kubos.simulation.life.gui;

import ch.qos.logback.classic.Logger;
import cz.kubos.simulation.life.Model;
import cz.kubos.simulation.life.gui.components.HexGrid;
import cz.kubos.simulation.life.gui.components.StatisticsPanel;
import cz.kubos.simulation.life.gui.components.controls.ButtonPanel;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The main window of the application
 *
 * Window class is a singleton class
 */
public class Window extends JFrame {
    public static final Color backgroundColor = new Color(35, 35, 35);
    public static final Color foregroundColor = Color.WHITE;
    public static Timer timer;
    private static Window instance = null;

    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(Window.class);
    private Window() {
        super("Life Simulator");

        Image icon = Toolkit.getDefaultToolkit().getImage("./graphics/LogoIcon.png");
        setIconImage(icon);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new ButtonPanel(), BorderLayout.EAST);
        contentPane.add(new StatisticsPanel(), BorderLayout.NORTH);


        HexGrid hexgrid = HexGrid.getInstance();

        contentPane.add(hexgrid, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(Window.this, "Are you sure you want to close the simulation?",
                        "Close window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    SharedData.setEnd(true);
                }
            }
        });

        setVisible(true);

        logger.info("Main window initialized");
    }

    public static Window getInstance(boolean check) {
        if (check) {
            if (instance == null) {
                synchronized (Window.class) {
                    if (instance == null) {
                        instance = new Window();
                    }
                }
            }
        }
        return instance;
    }

}
