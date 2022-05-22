package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonDeleteEntity extends JButton {
    public ButtonDeleteEntity () {
        super("DELETE ENTITY");

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = Window.getInstance(false);
                SharedData.setSpecie(null);
                SharedData.deleteEntity = true;
                window.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                SharedData.setFood(false);
            }
        });
    }
}
