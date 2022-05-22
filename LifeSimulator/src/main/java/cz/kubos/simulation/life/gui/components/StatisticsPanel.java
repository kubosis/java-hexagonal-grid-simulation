package cz.kubos.simulation.life.gui.components;

import cz.kubos.simulation.life.Model;
import cz.kubos.simulation.life.board.Board;
import cz.kubos.simulation.life.gui.SharedData;
import cz.kubos.simulation.life.gui.Window;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Statistics Panel displays the simulation statistics
 */
public class StatisticsPanel extends JPanel {
    private final JLabel statsLabel;
    private final JLabel actionLabel;
    public StatisticsPanel()
    {
        super();

        setBackground(Window.backgroundColor);
        setForeground(Window.foregroundColor);

        Border borderLine = BorderFactory.createLineBorder(Window.foregroundColor, 2);
        setBorder(borderLine);

        setLayout(new GridLayout(1,3));

        Board b = Board.getInstance();

        String boardStats = "World statistics: Board size is " +  b.rows + " x " + b.cols;
        JLabel label = new JLabel(boardStats);
        label.setForeground(Window.foregroundColor);
        add(label);

        statsLabel = new JLabel();
        actionLabel = new JLabel();
        statsLabel.setForeground(Window.foregroundColor);
        actionLabel.setForeground(Color.GREEN);
        actionLabel.setText("Last action: none");
        updateStats();
        add(statsLabel);
        add(actionLabel);

        int DELAY = 70; // fastest simulation speed
        Window.timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                updateStats();
            }
        });
        Window.timer.start();

    }

    public void updateStats() {
        String stats = "Entities: " + Model.getEntityCounter() + " ; day: " + SharedData.getSimulationAge() + ";";
        statsLabel.setText(stats);

        if (!SharedData.lastAction.isEmpty()) {
            actionLabel.setText("Last action: " + SharedData.lastAction.poll());
        }
    }



}
