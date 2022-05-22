package cz.kubos.simulation.life.gui.components.controls;

import cz.kubos.simulation.life.gui.Window;
import cz.kubos.simulation.life.gui.components.controls.buttons.ButtonExit;
import cz.kubos.simulation.life.gui.components.controls.buttons.ButtonLoad;
import cz.kubos.simulation.life.gui.components.controls.buttons.ButtonNewGame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Pannel for all gui buttons used in launcher
 */
public class LauncherButtonPanel extends JPanel {
    public LauncherButtonPanel() {
        super();
        setName("LBP");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        setLayout(new GridLayout(3,1));
        setBackground(Window.backgroundColor);

        Border borderLine = BorderFactory.createLineBorder(Window.foregroundColor, 2);
        setBorder(borderLine);

        add(new ButtonNewGame());
        add(new ButtonLoad());
        add(new ButtonExit());
    }
}
