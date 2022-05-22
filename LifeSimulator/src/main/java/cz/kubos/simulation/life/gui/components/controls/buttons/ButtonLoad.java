package cz.kubos.simulation.life.gui.components.controls.buttons;

import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class ButtonLoad extends JButton {
    public ButtonLoad () {
        super("LOAD SIMULATION");
        setBackground(Window.backgroundColor);
        setForeground(Window.foregroundColor);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoadFileChooser();
            }
        });
    }


    private static class LoadFileChooser extends JFrame  implements ActionListener{
        JList<String> list;
        public LoadFileChooser() {
            super("Choose simulation save to be loaded");

            setLayout(new BorderLayout());

            ArrayList<String> savedFiles = new ArrayList<>();
            File folder = new File("./saves/");
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles.length != 0){
                for (File f : listOfFiles) {
                    savedFiles.add(f.getName());
                }
            } else {
                savedFiles.add("No saved files");
            }

            setBackground(Window.backgroundColor);

            DefaultListModel<String> l1 = new DefaultListModel<>();
            l1.addAll(savedFiles);

            list = new JList<>(l1);
            add(list, BorderLayout.CENTER);

            JPanel jp = new JPanel();
            jp.setLayout(new GridLayout(1,2));

            JButton okButton = new JButton("OK");
            okButton.setBackground(Window.backgroundColor);
            okButton.setForeground(Window.foregroundColor);
            okButton.addActionListener(this);

            jp.add(okButton);

            JButton cancelButton = new JButton("CANCEL");
            cancelButton.setBackground(Window.backgroundColor);
            cancelButton.setForeground(Window.foregroundColor);
            cancelButton.addActionListener(this);

            jp.add(cancelButton);

            add(jp, BorderLayout.SOUTH);

            setSize(480, 340);
            setVisible(true);
            setLocationRelativeTo(null);

        }


        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("OK")) {
                String s = (String) list.getSelectedValue();
                if (s != null) {
                    SharedData.loadSimulation = new File("saves/" + s);
                    SharedData.lock.lock();
                    SharedData.cond.signal();
                    SharedData.lock.unlock();
                }
                this.dispose();
            } else if (e.getActionCommand().equals("CANCEL")) {
                this.dispose();
            }
        }
    }
}
