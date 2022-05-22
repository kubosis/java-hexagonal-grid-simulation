package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.Launcher;
import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonSave extends JButton {
    public ButtonSave () {
        super("SAVE");
        setBackground(Window.backgroundColor);
        setForeground(Window.foregroundColor);



        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Save the simulation?",
                        "Save?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    SharedData.save = true;
                    SharedData.lastAction.add("Simulation saved");

                    ButtonSave.this.setText("SAVED!");
                }
            }
        });

    }
}
