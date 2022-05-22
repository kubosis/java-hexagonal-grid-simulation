package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonPause extends JButton {
    private boolean running = true;
    private final String[] text = {"PAUSE", "RESUME"};
    public ButtonPause () {
        super();
        setText(text[0]);
        setBackground(Window.backgroundColor);
        setForeground(Window.foregroundColor);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = !running;
                setText(text[running ? 0 : 1]);
                SharedData.setRunning(running);
            }
        });
    }

}
