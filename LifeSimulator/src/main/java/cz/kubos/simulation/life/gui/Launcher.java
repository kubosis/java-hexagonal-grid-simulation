package cz.kubos.simulation.life.gui;

import ch.qos.logback.classic.Logger;
import cz.kubos.simulation.life.Model;
import cz.kubos.simulation.life.gui.components.controls.LauncherButtonPanel;
import cz.kubos.simulation.life.gui.components.controls.LauncherMenu;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * Launcher window of application
 *
 * Some parameters can only be changed here. Those are: the size of the board and number of automatically generated entities
 */
public class Launcher extends JFrame  {
    private static final Logger logger
            = (Logger) LoggerFactory.getLogger(Launcher.class);

    public Launcher () {
        super ("Life Simulator");

        Image icon = Toolkit.getDefaultToolkit().getImage("graphics/LogoIcon.png");
        setIconImage(icon);

        setSize(1200,800);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        Component c = new Component();
        add(c, BorderLayout.CENTER);

        setJMenuBar(new LauncherMenu());

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
                              @Override
                              public void windowClosing(WindowEvent e) {
                                  if (JOptionPane.showConfirmDialog(Launcher.this, "Are you sure you want to close the simulation?",
                                          "Close window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                      SharedData.setEnd(true);
                                      SharedData.lock.lock();
                                      SharedData.cond.signal();
                                      SharedData.lock.unlock();
                                  }
                              }
                          });

        setVisible(true);

        logger.info("Launcher initialized");

    }

    private static class Component extends JPanel {
        public Component() {
            super();
            //setLayout(new GridLayout(16,16));
            LauncherButtonPanel lbp = new LauncherButtonPanel();
            lbp.setPreferredSize(new Dimension(175,100));
            add(lbp);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Dimension d = getSize();
            try {
                Image Logo = ImageIO.read(new File("graphics/Logo.png")).getScaledInstance(d.width,d.height,0);
                Image background = ImageIO.read(new File("graphics/launcherBackground.jpg")).getScaledInstance(d.width,d.height,0);
                g.drawImage(background,0,0,null);
                g.drawImage(Logo, 0, 0, null);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            logger.info("Launcher repainted");
        }

    }
}
