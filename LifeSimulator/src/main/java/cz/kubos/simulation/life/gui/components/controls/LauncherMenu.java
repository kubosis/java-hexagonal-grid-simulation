package cz.kubos.simulation.life.gui.components.controls;

import cz.kubos.simulation.life.gui.SharedData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu for launcher
 */
public class LauncherMenu extends JMenuBar {
    public LauncherMenu()
    {
        super();

        JMenu settingsMenu = new JMenu("Settings");
        JMenu helpMenu = new JMenu("Help");

        JMenu boardMenu = new JMenu("Board");

        settingsMenu.add(boardMenu);


        JMenuItem boardRows = new JMenuItem("Rows");
        JMenuItem boardCols = new JMenuItem("Cols");

        boardRows.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePopUp("board rows");
            }
        });

        boardCols.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePopUp("board cols");
            }
        });

        boardMenu.add(boardRows);
        boardMenu.add(boardCols);

        JMenuItem entityCount = new JMenuItem("Entity count");
        entityCount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePopUp("Count of spawned entities");
            }
        });

        settingsMenu.add(entityCount);

        String[] help = {"To start new simulation,", "select new simulation option.", "To change board size", "or entity count, go to settings."};
        for (String s : help) {
            helpMenu.add(s);
        }

        add(settingsMenu);
        add(helpMenu);

    }

    private static class ChangePopUp extends JDialog {
        private final int mode;
        public ChangePopUp(String what)
        {
            super(null, "Change " + what, ModalityType.APPLICATION_MODAL);
            int initialValue;
            int minimumValue;
            int maximumValue;
            if (what.equals("board cols")) {
                mode = 0;
                initialValue = SharedData.cols;
                minimumValue = 1;
                maximumValue = 100;
            } else if (what.equals(("board rows"))) {
                mode = 1;
                initialValue = SharedData.rows;
                minimumValue = 1;
                maximumValue = 100;
            } else { // change entity count
                mode = 2;
                initialValue = SharedData.getMaxEntities();
                minimumValue = 0;
                maximumValue = Math.max(SharedData.cols * SharedData.rows / 4, 2);
            }
            setLayout(new GridLayout(1,3));
            JLabel label = new JLabel(what + ": ");
            add(label);


            SpinnerModel value =
                    new SpinnerNumberModel(initialValue, //initial value
                            minimumValue, //minimum value
                            maximumValue, //maximum value
                            1); //step
            JSpinner spinner = new JSpinner(value);
            //spinner.setBounds(100,100,50,30);
            add(spinner);

            JButton ok = new JButton("OK");
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int val = (Integer) spinner.getValue();

                    switch (getMode()) {
                        case 0 -> SharedData.cols = val;
                        case 1 -> SharedData.rows = val;
                        case 2 -> SharedData.maxEntities = val;
                    }

                    if (getMode() != 2) {
                        SharedData.maxEntities = SharedData.rows * SharedData.cols / 40;
                    }

                    dispose();
                }
            });
            add(ok);

            setSize(320,80);
            setAlwaysOnTop(true);

            setLocationRelativeTo(null);

            setVisible(true);

        }

        public int getMode()
        {
            return mode;
        }
    }


}
