package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNewGame extends JButton {
    public ButtonNewGame () {
        super("NEW SIMULATION");
        setBackground(Window.backgroundColor);
        setForeground(Window.foregroundColor);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SharedData.lock.lock();
                SharedData.cond.signal();
                SharedData.lock.unlock();
            }
        });
    }
}
