package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonExit extends JButton {
    public ButtonExit () {
        super("EXIT");
        setBackground(Window.backgroundColor);
        setForeground(Window.foregroundColor);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window win = Window.getInstance(false);
                if (JOptionPane.showConfirmDialog(win, "Are you sure you want to close the simulation?",
                        "Close window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    SharedData.setEnd(true);

                    /* in case of this is in launcher */
                    if (getParent().getName().equals("LBP")) {
                        SharedData.lock.lock();
                        SharedData.cond.signal();
                        SharedData.lock.unlock();
                    }
                }
            }
        });
    }
}
