package cz.kubos.simulation.life.gui.components.controls;

import cz.kubos.simulation.life.entity.Species;
import cz.kubos.simulation.life.gui.Window;
import cz.kubos.simulation.life.gui.components.controls.buttons.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Panel of all gui buttons that are used
 */
public class ButtonPanel extends JPanel {
    public ButtonPanel() {
        super();
        setName("BP");
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        setLayout(new GridLayout(20,1));
        setBackground(Window.backgroundColor);

        Border borderLine = BorderFactory.createLineBorder(Window.foregroundColor, 2);
        setBorder(borderLine);

        add(new ButtonPause());
        add(new ButtonSave());

        for (Species s : Species.values()) {
            add(new ButtonEntity(s));
        }

        add(new ButtonDeleteEntity());

        add(new ButtonFood());

        add(new ButtonExit(), -1);

    }
}
