package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.entity.Species;
import cz.kubos.simulation.life.gui.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ButtonEntity extends JButton {
    private final Species specie;
    private final Cursor cursor;

    public ButtonEntity(Species s) {
        super();
        specie = s;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image scaledEntityImage = specie.image.getScaledInstance(38,38,0);
        cursor = toolkit.createCustomCursor(scaledEntityImage, new Point(getX(),
                getY()), "img");

        setIcon(new ImageIcon(scaledEntityImage));

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SharedData.setSpecie(specie);
                SharedData.setFood(false);
                SharedData.deleteEntity = false;
                Window window = Window.getInstance(false);
                window.setCursor(cursor);
            }
        });
    }
}
