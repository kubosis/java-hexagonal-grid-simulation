package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.board.Biotope;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFood extends JButton {
    private final Cursor cursor;

    public ButtonFood() {
        super("SET FOOD");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.getImage(Biotope.MEADOW.foodImPath).getScaledInstance(32,32,0);
        cursor = toolkit.createCustomCursor(cursorImage, new Point(getX(),
                getY()), "img");

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = Window.getInstance(false);
                SharedData.setSpecie(null);
                SharedData.deleteEntity = false;
                window.setCursor(cursor);
                SharedData.setFood(true);
            }
        });
    }
}
